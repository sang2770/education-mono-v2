package com.sang.nv.education.iam.application.service.impl;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.commonutil.StrUtils;
import com.sang.commonutil.StringUtil;
import com.sang.nv.education.iam.application.dto.request.User.ChangePasswordRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserCreateRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserExportRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserSearchRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserUpdateRoleRequest;
import com.sang.nv.education.iam.application.dto.response.ImportResult;
import com.sang.nv.education.iam.application.mapper.IamAutoMapper;
import com.sang.nv.education.iam.application.mapper.IamAutoMapperQuery;
import com.sang.nv.education.iam.application.service.ExcelService;
import com.sang.nv.education.iam.application.service.UserService;
import com.sang.nv.education.iam.domain.Role;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.iam.domain.command.UserCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.query.UserSearchQuery;
import com.sang.nv.education.iam.domain.repository.UserDomainRepository;
import com.sang.nv.education.iam.infrastructure.persistence.entity.ClassEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.RoleEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.UserEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.UserRoleEntity;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.RoleEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.UserEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.repository.ClassesEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.UserRoleEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.readmodel.StatisticUser;
import com.sang.nv.education.iam.infrastructure.support.enums.RoleStatus;
import com.sang.nv.education.iam.infrastructure.support.exception.BadRequestError;
import com.sang.nv.education.iam.infrastructure.support.exception.NotFoundError;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserEntityRepository userEntityRepository;
    private final UserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final IamAutoMapperQuery iamAutoMapperQuery;
    private final UserDomainRepository userDomainRepository;
    private final IamAutoMapper iamAutoMapper;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final RoleEntityRepository roleEntityRepository;
    private final ClassesEntityRepository classesEntityRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final ExcelService excelService;

    public UserServiceImpl(UserEntityRepository userEntityRepository, UserEntityMapper userEntityMapper, PasswordEncoder passwordEncoder,
                           IamAutoMapperQuery iamAutoMapperQuery, UserDomainRepository userDomainRepository, IamAutoMapper iamAutoMapper, UserRoleEntityRepository userRoleEntityRepository, RoleEntityRepository roleEntityRepository, ClassesEntityRepository classesEntityRepository, RoleEntityMapper roleEntityMapper, ExcelService excelService) {
        this.userEntityRepository = userEntityRepository;
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
        this.iamAutoMapperQuery = iamAutoMapperQuery;
        this.userDomainRepository = userDomainRepository;
        this.iamAutoMapper = iamAutoMapper;
        this.userRoleEntityRepository = userRoleEntityRepository;
        this.roleEntityRepository = roleEntityRepository;
        this.classesEntityRepository = classesEntityRepository;
        this.roleEntityMapper = roleEntityMapper;
        this.excelService = excelService;
    }

    @Override
    public User ensureExisted(String id) {
        return this.userDomainRepository.findById(id).orElseThrow(() ->
                new ResponseException(NotFoundError.USER_NOT_FOUND));
    }

    @Override
    public User create(UserCreateRequest request) {
        // valid base user information
        Optional<UserEntity> userEntityByPhone = userEntityRepository.findByPhoneNumber(request.getPhoneNumber());
        if (userEntityByPhone.isPresent()) {
            throw new ResponseException(BadRequestError.USER_PHONE_NUMBER_EXITED);
        }
        UserCreateOrUpdateCmd cmd = this.iamAutoMapper.from(request);
        validateBaseInformation(cmd, Optional.empty());

        // valid username
        if (userEntityRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseException(BadRequestError.USER_USERNAME_EXITED);
        }

        // valid password
        if (!StrUtils.isBlank(request.getPassword()) && !StringUtil.validatePassword(request.getPassword())) {
            throw new ResponseException(BadRequestError.PASSWORD_NOT_STRONG);
        }

        if (!StrUtils.isBlank(request.getPassword())
                && !StrUtils.isBlank(request.getRepeatPassword())
                && !Objects.equals(request.getPassword(), request.getRepeatPassword())) {
            throw new ResponseException(BadRequestError.REPEAT_PASSWORD_DOES_NOT_MATCH);
        }

        cmd.setPassword(this.passwordEncoder.encode(cmd.getPassword()));
        List<RoleEntity> roleEntities = roleEntityRepository.findAllByStatus(RoleStatus.ACTIVE);
        User user = new User(cmd, roleEntityMapper.toDomain(roleEntities));
        this.userDomainRepository.save(user);
        return user;
    }

    @Override
    public User update(String userId, UserUpdateRequest request) {
        User user = this.ensureExisted(userId);
        UserCreateOrUpdateCmd cmd = this.iamAutoMapper.from(request);
        validateBaseInformation(cmd, Optional.of(userId));
        this.userDomainRepository.getById(userId);
        List<RoleEntity> roleEntities = roleEntityRepository.findAllByStatus(RoleStatus.ACTIVE);
        user.update(cmd, roleEntityMapper.toDomain(roleEntities));
        this.userDomainRepository.save(user);
        return user;
    }

    @Override
    public void delete(String id) {
        User user = this.ensureExisted(id);
        user.deleted();
        this.userDomainRepository.save(user);
    }

    @Override
    public PageDTO<User> search(UserSearchRequest request) {
        UserSearchQuery query = this.iamAutoMapperQuery.toQuery(request);
        if (!CollectionUtils.isEmpty(request.getDepartmentIds()) && CollectionUtils.isEmpty(request.getClassIds())) {
            List<ClassEntity> classEntities = this.classesEntityRepository.findAllByDepartmentIds(request.getDepartmentIds());
            List<String> classIds = classEntities.stream().map(ClassEntity::getId).collect(Collectors.toList());
            query.setClassIds(classIds);
        }
        Long count = this.userEntityRepository.countUser(query);
        if (count == 0L) {
            return PageDTO.empty();
        }
        List<User> users = this.userEntityMapper.toDomain(this.userEntityRepository.search(query));
        this.enrichUser(users);
        return PageDTO.of(users, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public void deleteByIds(List<String> ids) {
        List<UserEntity> userEntities = userEntityRepository.findByIds(ids);
        List<User> users = userEntityMapper.toDomain(userEntities);
        for (String userId : ids) {
            Optional<User> optionalUser = users.stream().filter(u -> u.getId().equals(userId)).findFirst();
            if (optionalUser.isEmpty()) {
                throw new ResponseException(NotFoundError.USER_NOT_FOUND);
            }
            User user = optionalUser.get();
            user.deleted();
        }
        this.userEntityRepository.saveAll(this.userEntityMapper.toEntity(users));
    }

    @Override
    public List<User> findByIds(List<String> ids) {
        List<UserEntity> userEntities = this.userEntityRepository.findByIds(ids);
        List<User> users = this.userEntityMapper.toDomain(userEntities);
        this.enrichUser(users);
        return users;
    }

    private void enrichUser(List<User> users) {
        List<String> userIds = users.stream().map(User::getId).collect(Collectors.toList());
        // enrich roles
        List<RoleEntity> roleEntities = this.roleEntityRepository.findAllByStatus(RoleStatus.ACTIVE);
        List<Role> roles = this.roleEntityMapper.toDomain(roleEntities);
        List<UserRoleEntity> userRoleEntities = this.userRoleEntityRepository.findAllByUserIds(userIds);
        users.forEach(user -> {
            // enrich role
            List<String> roleIdsOfUser = userRoleEntities.stream()
                    .filter(userRole -> userRole.getUserId().equals(user.getId()))
                    .map(UserRoleEntity::getRoleId)
                    .distinct().collect(Collectors.toList());
            List<Role> rolesOfUser = roles.stream()
                    .filter(role -> roleIdsOfUser.contains(role.getId())).collect(Collectors.toList());
            user.enrichRoles(rolesOfUser);
        });
    }

    @Override
    public void active(String userId) {
        User user = ensureExisted(userId);
        user.active();
        this.userDomainRepository.save(user);
    }

    @Override
    public void inactive(String userId) {
        User user = ensureExisted(userId);
        user.inactive();
        this.userDomainRepository.save(user);
    }

    @Override
    public User getUserById(String userId) {
        User user = this.userDomainRepository.getById(userId);

//        if (Objects.nonNull(user.getAvatarFileId())) {
//            Response<FileDTO> responseFiles = storageClient.findById(user.getAvatarFileId());
//            if (responseFiles.isSuccess() && Objects.nonNull(responseFiles.getData())) {
//                user.enrichViewUrlFile(responseFiles.getData().getFilePath());
//            }
//        }
        return user;
    }

    @Override
    public User changePassword(String userId, ChangePasswordRequest request) {
        User user = this.ensureExisted(userId);
        String newEncodedPassword = this.passwordEncoder.encode(request.getNewPassword());
        user.changePassword(newEncodedPassword);
        this.userDomainRepository.save(user);
        return user;
    }

    @Override
    public PageDTO<User> autoComplete(UserSearchRequest request) {
        return null;
    }

    public void validateBaseInformation(UserCreateOrUpdateCmd request, Optional<String> userId) {
        // valid user by phoneNumber
        Optional<UserEntity> userEntityByPhone = userEntityRepository
                .findByPhoneNumber(request.getPhoneNumber());
        if (userEntityByPhone.isPresent() && !Objects.equals(userEntityByPhone.get().getId(), userId.get())) {
            throw new ResponseException(BadRequestError.USER_PHONE_NUMBER_EXITED);
        }

        // valid user by email
        Optional<UserEntity> userEntityByEmail = userEntityRepository.findByEmail(request.getEmail());
        if (userEntityByEmail.isPresent() && !Objects.equals(userEntityByEmail.get().getId(), userId.get())) {
            throw new ResponseException(BadRequestError.USER_EMAIL_EXITED);
        }

//        if (Objects.nonNull(request.getAvatarFileId())) {
//            String fileId =request.getAvatarFileId();
//            Response<FileDTO> responseFiles = storageClient.findById(fileId);
//            if (!responseFiles.isSuccess() || Objects.isNull(responseFiles.getData())) {
//                throw new ResponseException(BadRequestError.FILE_NOT_EXISTED);
//            }
//            request.setAvatarFileId(fileId);
//            request.setAvatarFileViewUrl(responseFiles.getData().getFilePath());
//        }
    }

    @Override
    public void exportUsers(UserSearchRequest request, HttpServletResponse response) {
        this.excelService.exportUsers(request, response);
    }

    @Override
    public void downloadTemplateImportUsers(HttpServletResponse response) {
        this.excelService.downloadUserTemplate(response);
    }

    @Override
    public ImportResult importUser(MultipartFile file, HttpServletResponse response) {
        return this.excelService.importUser(file, response);
    }

    @Override
    public Integer countUser(List<String> roomIds) {
        return this.userEntityRepository.countUser(UserSearchQuery.builder().roomIds(roomIds).build()).intValue();
    }

    @Override
    public List<StatisticUser> statisticsUser(Integer year) {
        return this.userEntityRepository.statisticsUser(year);
    }

    @Override
    public User updateRole(String id, UserUpdateRoleRequest request) {
        User user = this.ensureExisted(id);
        List<RoleEntity> roleEntities = roleEntityRepository.findAllByStatus(RoleStatus.ACTIVE);
        user.updateRole(request.getRoleIds(), roleEntityMapper.toDomain(roleEntities));
        this.userDomainRepository.save(user);
        return user;
    }
}
