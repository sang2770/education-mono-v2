package com.sang.nv.education.iam.application.service.Impl;


import com.sang.commonclient.client.storage.StorageClient;
import com.sang.commonmodel.auth.UserAuthority;
import com.sang.commonmodel.error.enums.AuthenticationError;
import com.sang.commonmodel.error.enums.NotFoundError;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonweb.security.AuthorityService;
import com.sang.commonweb.support.SecurityUtils;
import com.sang.nv.education.iamapplication.config.AuthenticationProperties;
import com.sang.nv.education.iamapplication.config.TokenProvider;
import com.sang.nv.education.iamapplication.dto.request.Auth.LoginRequest;
import com.sang.nv.education.iamapplication.dto.request.Auth.LogoutRequest;
import com.sang.nv.education.iamapplication.dto.request.Auth.RefreshTokenRequest;
import com.sang.nv.education.iamapplication.dto.response.AuthToken;
import com.sang.nv.education.iamapplication.mapper.AutoMapper;
import com.sang.nv.education.iamapplication.service.AccountService;
import com.sang.nv.education.iamapplication.service.AuthFailCacheService;
import com.sang.nv.education.iamdomain.User;
import com.sang.nv.education.iamdomain.repository.UserDomainRepository;
import com.sang.nv.education.iaminfrastructure.persistence.entity.UserEntity;
import com.sang.nv.education.iaminfrastructure.persistence.mapper.UserEntityMapper;
import com.sang.nv.education.iaminfrastructure.persistence.repository.UserEntityRepository;
import com.sang.nv.education.iaminfrastructure.support.enums.UserStatus;
import com.sang.nv.education.iaminfrastructure.support.enums.UserType;
import com.sang.nv.education.iaminfrastructure.support.exception.BadRequestError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {


    private final UserDomainRepository userDomainRepository;
    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final AutoMapper autoMapper;
    private final TokenProvider tokenProvider;
    private final AuthenticationProperties authenticationProperties;
    private final AuthenticationManager authenticationManager;
    private final AuthorityService authorityService;
    private final AuthFailCacheService authFailCacheService;
    private final StorageClient storageClient;

    @Override
    public AuthToken login(LoginRequest request) {
        return this.authenticateUser(request, true);
    }

    @Override
    public AuthToken loginClient(LoginRequest request) {
        return this.authenticateUser(request, false);
    }

    @Override
    public AuthToken refreshToken(RefreshTokenRequest request) {
        if (!this.tokenProvider.validateRefreshToken(request.getRefreshToken())) {
            throw new ResponseException(AuthenticationError.INVALID_REFRESH_TOKEN);
        }

        String userId = this.tokenProvider.getSubject(request.getRefreshToken());
        UserEntity userEntity = this.userEntityRepository.findById(userId).orElseThrow(() ->
                new ResponseException(NotFoundError.USER_NOT_FOUND));
        if (!UserStatus.ACTIVE.equals(userEntity.getStatus())) {
            authFailCacheService.checkLoginFail(userEntity.getUsername());
            throw new ResponseException(BadRequestError.LOGIN_FAIL_BLOCK_ACCOUNT);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity.getUsername(),
                "",
                new ArrayList<>());
        String accessToken = this.tokenProvider.createToken(authentication, userEntity.getId());
        String refreshToken = this.tokenProvider.createRefreshToken(userEntity.getId());

        long expiresIn = this.authenticationProperties.getAccessTokenExpiresIn().toSeconds();

        return AuthToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(AuthToken.TOKEN_TYPE_BEARER)
                .expiresIn(expiresIn)
                .build();
    }

    @Override
    public User myProfile() {
        return currentAccount();
    }

    public String currentUserId() {
        Optional<String> currentUserLoginId = SecurityUtils.getCurrentUserLoginId();
        if (currentUserLoginId.isEmpty()) {
            throw new ResponseException(AuthenticationError.UNAUTHORISED);
        }
        return currentUserLoginId.get();
    }

    @Override
    public UserAuthority myAuthorities() {
        String me = currentUserId();
        return this.authorityService.getUserAuthority(me);
    }

    @Override
    public void logout(LogoutRequest logoutRequest) {
        // revoke device -> invalid token and refresh token
        Optional<String> optionalJwt = SecurityUtils.getCurrentUserJWT();
        if (optionalJwt.isPresent()) {
            String accessToken = optionalJwt.get();
            tokenProvider.invalidJwt(accessToken, logoutRequest.getRefreshToken());
        }
    }

    @Override
    public String currentUser() {
        Optional<String> currentUser = SecurityUtils.getCurrentUser();
        if (currentUser.isEmpty()) {
            throw new ResponseException(AuthenticationError.UNAUTHORISED);
        }
        return currentUser.get();
    }

    private User currentAccount() {
        String userId = currentUserId();
        User user = this.userDomainRepository.getById(userId);
//        if (Objects.nonNull(user.getAvatarFileId()))
//        {
//            Response<FileDTO> response = this.storageClient.findById(user.getAvatarFileId());
//            if (response.isSuccess() && Objects.nonNull(response.getData()))
//            {
//                user.enrichAvatarFileViewUrl(response.getData().getFilePath());
//            }
//        }
        return user;
    }

    private AuthToken authenticateUser(LoginRequest request, Boolean isManager) {
        log.warn("User {} start login", request.getUsername());

        // check account was locked
        if (authFailCacheService.isBlockedUser(request.getUsername())) {
            log.warn("User {} is blocked", request.getUsername());
            throw new ResponseException(BadRequestError.LOGIN_FAIL_BLOCK_ACCOUNT);
        }

        // check user
        Optional<UserEntity> optionalUserEntity = this.userEntityRepository.findByUsername(request.getUsername());
        if (optionalUserEntity.isEmpty()) {
            BadRequestError error = authFailCacheService.checkLoginFail(request.getUsername());
            log.warn("User login not found: {}", request.getUsername());
            if (error == null) {
                throw new BadCredentialsException("Bad credential!");
            } else {
                throw new ResponseException(error.getMessage(), error);
            }
        }

        UserEntity userEntity = optionalUserEntity.get();
        if (!UserStatus.ACTIVE.equals(userEntity.getStatus())) {
            log.warn("User login not activated: {}", request.getUsername());
            authFailCacheService.checkLoginFail(userEntity.getUsername());
            throw new ResponseException(BadRequestError.LOGIN_FAIL_BLOCK_ACCOUNT);
        }

        if (Objects.equals(userEntity.getIsRoot(), false) && isManager && !Objects.equals(userEntity.getUserType(), UserType.MANAGER))
        {
            log.warn("User login not manager: {}", request.getUsername());
            authFailCacheService.checkLoginFail(userEntity.getUsername());
            throw new ResponseException(BadRequestError.USER_NOT_PERMISSION_FAIL_ACCOUNT);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getUsername().toLowerCase(),
                request.getPassword(), new ArrayList<>());
        authentication = authenticationManager.authenticate(authentication);

        String accessToken = this.tokenProvider.createToken(authentication, userEntity.getId());
        long expiresIn = this.authenticationProperties.getAccessTokenExpiresIn().toSeconds();
        String refreshToken = null;
        if (request.isRememberMe()) {
            refreshToken = this.tokenProvider.createRefreshToken(userEntity.getId());
        }
        log.warn("User {} login success", request.getUsername());

        authFailCacheService.resetLoginFail(request.getUsername());
        return AuthToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType(AuthToken.TOKEN_TYPE_BEARER)
                .expiresIn(expiresIn)
                .build();
    }
}
