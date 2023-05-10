package com.sang.nv.education.iam.application.service.impl;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.commonmodel.mapper.util.PageableMapperUtil;
import com.sang.commonpersistence.support.SeqRepository;
import com.sang.commonpersistence.support.SqlUtils;
import com.sang.nv.education.iam.application.dto.request.Role.RoleCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.Role.RoleSearchRequest;
import com.sang.nv.education.iam.application.mapper.IamAutoMapper;
import com.sang.nv.education.iam.application.service.RoleService;
import com.sang.nv.education.iam.domain.Permission;
import com.sang.nv.education.iam.domain.Role;
import com.sang.nv.education.iam.domain.UserRole;
import com.sang.nv.education.iam.domain.command.RoleCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.repository.RoleDomainRepository;
import com.sang.nv.education.iam.infrastructure.persistence.entity.PermissionEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.RoleEntity;
import com.sang.nv.education.iam.infrastructure.persistence.entity.UserRoleEntity;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.RoleEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.UserRoleEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.RoleEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.UserRoleEntityRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleEntityRepository roleEntityRepository;
    private final PermissionEntityRepository permissionEntityRepository;
    private final RoleEntityMapper roleEntityMapper;
    private final PermissionEntityMapper permissionEntityMapper;
    private final IamAutoMapper autoMapper;
    private final RoleDomainRepository roleDomainRepository;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final UserRoleEntityMapper userRoleEntityMapper;
    private final SeqRepository seqRepository;

    @Override
    public PageDTO<Role> search(RoleSearchRequest request) {

        Pageable pageable = PageableMapperUtil.toPageable(request);

        Page<RoleEntity> roleEntityPage
                = this.roleEntityRepository.search(SqlUtils.encodeKeyword(request.getKeyword()), pageable);
        List<Role> roles = this.roleEntityMapper.toDomain(roleEntityPage.toList());

        return PageDTO.of(roles, request.getPageIndex(), request.getPageSize(), roleEntityPage.getTotalElements());
    }

    @Override
    @Transactional
    public Role create(RoleCreateOrUpdateRequest request) {
        RoleCreateOrUpdateCmd cmd = this.autoMapper.from(request);
        List<Permission> permissions = existedPermissions();
        cmd.setCode(this.seqRepository.generateRoleCode());
        Role role = new Role(cmd, permissions);
        this.roleDomainRepository.save(role);
        return role;
    }

    @Override
    @Transactional
    public Role update(String roleId, RoleCreateOrUpdateRequest request) {
        Role role = this.roleDomainRepository.getById(roleId);
        RoleCreateOrUpdateCmd cmd = this.autoMapper.from(request);
        List<Permission> permissions = existedPermissions();
        role.update(cmd, permissions);
        this.roleDomainRepository.save(role);
        return role;
    }

    @Override
    @Transactional
    public void delete(String roleId) {
        Role role = this.roleDomainRepository.getById(roleId);
        role.deleted();
        this.roleDomainRepository.save(role);
    }

    @Override
    @Transactional
    public void active(String id) {
        Role role = this.roleDomainRepository.getById(id);
        role.active();
        this.roleDomainRepository.save(role);
    }

    @Override
    @Transactional
    public void inactive(String id) {
        Role role = this.roleDomainRepository.getById(id);
        role.inactive();
        this.roleDomainRepository.save(role);
    }

    @Override
    public List<UserRole> findUserRoleByRoleIds(FindByIdsRequest request) {
        List<UserRoleEntity> userRoleEntities = this.userRoleEntityRepository.findAllByRoleIds(request.getIds());
        List<UserRole> userRoles = this.userRoleEntityMapper.toDomain(userRoleEntities);
        return userRoles;
    }

    @Override
    public Role getById(String id) {
        return this.roleDomainRepository.getById(id);
    }

    @Override
    public List<Role> findByUserId(String userId) {
        List<UserRoleEntity> userRoles = this.userRoleEntityRepository.findAllByUserIds(List.of(userId));
        List<String> roleIds = userRoles.stream().map(UserRoleEntity::getRoleId).collect(Collectors.toList());
        List<RoleEntity> roleEntities = this.roleEntityRepository.findAllByIds(roleIds);
        return this.roleEntityMapper.toDomain(roleEntities);
    }

    private List<Permission> existedPermissions() {
        List<PermissionEntity> permissionEntities = this.permissionEntityRepository.findAllActivated();
        if (permissionEntities == null) {
            return new ArrayList<>();
        }
        return this.permissionEntityMapper.toDomain(permissionEntities);
    }


}
