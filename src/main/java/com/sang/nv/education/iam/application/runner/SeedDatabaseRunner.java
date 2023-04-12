package com.sang.nv.education.iam.application.runner;


import com.sang.commonmodel.enums.Scope;
import com.sang.nv.education.iam.domain.Permission;
import com.sang.nv.education.iam.domain.Role;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.iam.domain.command.RoleCreateOrUpdateCmd;
import com.sang.nv.education.iam.infrastructure.persistence.entity.PermissionEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.RoleEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.UserEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.UserRoleEntity;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.RoleEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.UserEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.UserRoleEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.UserEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.UserRoleEntityRepository;
import com.sang.nv.education.iam.infrastructure.support.enums.ResourceCategory;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SeedDatabaseRunner implements CommandLineRunner {

    private final PermissionEntityRepository permissionEntityRepository;
    private final PermissionEntityMapper permissionEntityMapper;
    private final RoleEntityRepository roleEntityRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final UserEntityRepository userEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserEntityMapper userEntityMapper;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final UserRoleEntityMapper userRoleEntityMapper;

    @Override
    @Transactional
    public void run(String... args) {
        this.initPermission();
        this.initAdmin();
    }

    @Transactional
    public void initPermission() {
        List<PermissionEntity> activePermissionEntities = this.permissionEntityRepository.findAllActivated();

        ResourceCategory[] resourceCategories = ResourceCategory.values();
        List<Permission> permissions = new ArrayList<>();
        for (ResourceCategory resource : resourceCategories) {
            if (CollectionUtils.isEmpty(resource.getScopes())) {
                continue;
            }
            for (Scope scope : resource.getScopes()) {
                Optional<PermissionEntity> optionalPermissionEntity = activePermissionEntities.stream()
                        .filter(p -> p.getResourceCode().equals(resource.getResourceCode())
                                && p.getScope().equals(scope)).findFirst();
                if (optionalPermissionEntity.isPresent()) {
                    continue;
                }

                String scopeName = this.getScopeNameByScope(scope);
                Permission permission = new Permission(resource.getResourceCode(), scope, scopeName, resource.getPriority());
                permissions.add(permission);
            }
        }

        List<PermissionEntity> permissionEntities = this.permissionEntityMapper.toEntity(permissions);
        this.permissionEntityRepository.saveAll(permissionEntities);
    }

    private String getScopeNameByScope(Scope scope) {
        String scopeName = "Xem";
        if (Scope.VIEW.equals(scope)) {
            scopeName = "Xem";
        } else if (Scope.REPORT.equals(scope)) {
            scopeName = "Báo cáo";
        } else if (Scope.CREATE.equals(scope)) {
            scopeName = "Tạo";
        } else if (Scope.UPDATE.equals(scope)) {
            scopeName = "Cập nhật";
        } else if (Scope.DELETE.equals(scope)) {
            scopeName = "Xóa";
        }
        return scopeName;
    }

    @Transactional
    public RoleEntity initAdminRole() {
        List<RoleEntity> roleEntities = roleEntityRepository.findAllRootRole();
        if (!CollectionUtils.isEmpty(roleEntities)) {
            return roleEntities.get(0);
        }
        RoleCreateOrUpdateCmd cmd = RoleCreateOrUpdateCmd.builder()
                .code("Administrator")
                .name("Quản trị hệ thống")
                .isRoot(true)
                .description("Vai trò quản trị hệ thống")
                .build();

        Role role = new Role(cmd, null);
        RoleEntity roleEntity = this.roleEntityMapper.toEntity(role);
        this.roleEntityRepository.save(roleEntity);
        return roleEntity;
    }

    @Transactional
    public void initAdmin() {
        RoleEntity roleEntity = initAdminRole();
        String username = "sang";
        if (this.userEntityRepository.findByUsername(username).isPresent()) {
            return;
        }
        String encodedPassword = this.passwordEncoder.encode("123456aA@");
        User user = new User(username, encodedPassword, "Quản trị hệ thống", roleEntity.getId(), true, UserType.MANAGER);
        UserEntity userEntity = this.userEntityMapper.toEntity(user);
        List<UserRoleEntity> userRoleEntities = this.userRoleEntityMapper.toEntity(user.getUserRoles());
        this.userEntityRepository.save(userEntity);
        this.userRoleEntityRepository.saveAll(userRoleEntities);
    }
}
