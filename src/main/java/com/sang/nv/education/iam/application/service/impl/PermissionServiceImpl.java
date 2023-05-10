package com.sang.nv.education.iam.application.service.impl;

import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.i18n.LocaleStringService;
import com.sang.nv.education.iam.application.dto.request.Permission.PermissionUpdateRequest;
import com.sang.nv.education.iam.application.mapper.IamAutoMapper;
import com.sang.nv.education.iam.application.mapper.IamAutoMapperQuery;
import com.sang.nv.education.iam.application.service.PermissionService;
import com.sang.nv.education.iam.domain.Permission;
import com.sang.nv.education.iam.domain.command.PermissionUpdateCmd;
import com.sang.nv.education.iam.infrastructure.persistence.entity.PermissionEntity;
import com.sang.nv.education.iam.infrastructure.persistence.mapper.PermissionEntityMapper;
import com.sang.nv.education.iam.infrastructure.persistence.repository.PermissionEntityRepository;
import com.sang.nv.education.iam.infrastructure.persistence.repository.RolePermissionEntityRepository;
import com.sang.nv.education.iam.infrastructure.support.enums.ResourceCategory;
import com.sang.nv.education.iam.infrastructure.support.exception.NotFoundError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    private final PermissionEntityRepository permissionEntityRepository;
    private final PermissionEntityMapper permissionEntityMapper;
    private final LocaleStringService localeStringService;
    private final RolePermissionEntityRepository rolePermissionEntityRepository;
    private final IamAutoMapper autoMapper;
    private final IamAutoMapperQuery autoMapperQuery;

    public PermissionServiceImpl(PermissionEntityRepository permissionEntityRepository,
                                 PermissionEntityMapper permissionEntityMapper,
                                 LocaleStringService localeStringService,
                                 RolePermissionEntityRepository rolePermissionEntityRepository, IamAutoMapper autoMapper, IamAutoMapperQuery autoMapperQuery) {
        this.permissionEntityRepository = permissionEntityRepository;
        this.permissionEntityMapper = permissionEntityMapper;
        this.localeStringService = localeStringService;
        this.rolePermissionEntityRepository = rolePermissionEntityRepository;
        this.autoMapper = autoMapper;
        this.autoMapperQuery = autoMapperQuery;
    }

    /**
     * find all Permission
     *
     * @return list permission
     */
    @Override
    public List<Permission> findAll() {
        List<PermissionEntity> permissionEntities = this.permissionEntityRepository.findAllActivated();
        ResourceCategory[] resourceCategories = ResourceCategory.values();
        List<Permission> permissions = this.permissionEntityMapper.toDomain(permissionEntities);
        permissions.forEach(p -> {
            for (ResourceCategory perType : resourceCategories) {
                if (p.getResourceCode().equals(perType.getResourceCode())) {
                    p.enrichResourceName(perType.getResourceName());
                }
            }
            if (!StringUtils.hasLength(p.getResourceName())) {
                p.enrichResourceName(p.getResourceCode());
            }
        });
        return permissions;
    }

    @Override
    @Transactional
    public Permission update(String id, PermissionUpdateRequest request) {
        PermissionUpdateCmd cmd = autoMapper.from(request);
        Permission permission = permissionEntityMapper
                .toDomain(permissionEntityRepository.findById(id).orElseThrow(() ->
                        new ResponseException(NotFoundError.PERMISSION_NOT_FOUND)));
        permission.update(cmd);
        this.permissionEntityRepository
                .save(this.permissionEntityMapper.toEntity(permission));
        return permission;
    }

    @Override
    @Transactional
    public void delete(String id) {
    }
}