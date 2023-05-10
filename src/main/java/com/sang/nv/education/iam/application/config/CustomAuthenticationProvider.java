package com.sang.nv.education.iam.application.config;


import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.iam.application.service.AuthFailCacheService;
import com.sang.nv.education.iam.infrastructure.persistence.entity.UserEntity;
import com.sang.nv.education.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.sang.nv.education.iam.infrastructure.support.exception.BadRequestError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthFailCacheService authFailCacheService;

    public CustomAuthenticationProvider(UserEntityRepository userEntityRepository,
                                        PasswordEncoder passwordEncoder,
                                        AuthFailCacheService authFailCacheService
    ) {
        this.userEntityRepository = userEntityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authFailCacheService = authFailCacheService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("Authenticating {}", authentication);
        String username = authentication.getName();
        String credentials = (String) authentication.getCredentials();

        Optional<UserEntity> optionalUserEntity = userEntityRepository.findByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;

        if (authenticationToken.isAuthenticated()) {
            if (optionalUserEntity.isEmpty()) {
                // login fail -> increase number of block
                BadRequestError error = authFailCacheService.checkLoginFail(username);
                onError(error);
            }
            UserEntity userEntity = optionalUserEntity.get();
            String passwordStore = userEntity.getPassword();
            boolean matches = passwordEncoder.matches(credentials, passwordStore);

            //check UserName Password:
            if (!matches) {
                // login fail -> increase number of block
                BadRequestError error = authFailCacheService.checkLoginFail(username);
                onError(error);
            }
        }
        return new UsernamePasswordAuthenticationToken(username, authentication.getCredentials(), new ArrayList<>());
    }

    private void onError(BadRequestError error) {
        if (error == null) {
            throw new BadCredentialsException("Bad credential!");
        } else {
            throw new ResponseException(error.getMessage(), error);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
