package com.sang.nv.education.iam.application.service;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.nv.education.iam.application.dto.request.Role.RoleCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.Role.RoleSearchRequest;
import com.sang.nv.education.iam.domain.Role;
import com.sang.nv.education.iam.domain.UserRole;

import java.util.List;

public interface RoleService {

    Role getById(String id);

    PageDTO<Role> search(RoleSearchRequest request);

    Role create(RoleCreateOrUpdateRequest request);

    Role update(String userId, RoleCreateOrUpdateRequest request);

    void delete(String id);

    void active(String id);

    void inactive(String id);

    List<UserRole> findUserRoleByRoleIds(FindByIdsRequest request);

    List<Role> findByUserId(String userId);
}
