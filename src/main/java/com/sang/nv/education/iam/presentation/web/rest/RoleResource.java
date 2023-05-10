package com.sang.nv.education.iam.presentation.web.rest;

import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.commonmodel.validator.anotations.ValidatePaging;
import com.sang.nv.education.iam.application.dto.request.Role.RoleCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.Role.RoleSearchRequest;
import com.sang.nv.education.iam.domain.Role;
import com.sang.nv.education.iam.domain.UserRole;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "Role Resource")
@RequestMapping("/api")
@Validated
public interface RoleResource {

    @ApiOperation(value = "Search user")
    @GetMapping("/roles")
    @PreAuthorize("hasPermission(null, 'user:view')")
    PagingResponse<Role> search(@ValidatePaging(allowedSorts = {"id", "createdAt", "code", "name", "createdBy", "lastModifiedAt"})
                                RoleSearchRequest request);

    @ApiOperation(value = "Create role")
    @PostMapping("/roles")
    @PreAuthorize("hasPermission(null, 'role:create')")
    Response<Role> create(@RequestBody @Valid RoleCreateOrUpdateRequest request);

    @ApiOperation(value = "Update role")
    @PostMapping("/roles/{id}/update")
    @PreAuthorize("hasPermission(null, 'role:update')")
    Response<Role> update(@PathVariable String id, @RequestBody @Valid RoleCreateOrUpdateRequest request);

    @ApiOperation(value = "Find role")
    @GetMapping("/roles/{id}")
    @PreAuthorize("hasPermission(null, 'role:view')")
    Response<Role> findById(@PathVariable String id);

    @ApiOperation(value = "Delete role")
    @PostMapping("/roles/{id}/delete")
    @PreAuthorize("hasPermission(null, 'role:delete')")
    Response<Role> delete(@PathVariable String id);

    @ApiOperation(value = "Active Role")
    @PostMapping("/roles/{id}/active")
    @PreAuthorize("hasPermission(null, 'role:update')")
    Response<Boolean> active(@PathVariable String id);

    @ApiOperation(value = "Inactive Role")
    @PostMapping("/roles/{id}/inactive")
    @PreAuthorize("hasPermission(null, 'role:update')")
    Response<Boolean> inactive(@PathVariable String id);

    @ApiOperation(value = "Search user roles by role ids")
    @PostMapping("/roles/user-roles/find-by-role-ids")
    @PreAuthorize("hasPermission(null, 'role:view')")
    Response<List<UserRole>> findALlByRoleIds(@RequestBody FindByIdsRequest request);


    @ApiOperation(value = "Find role by userId")
    @GetMapping("/roles/{userId}/user-roles")
    @PreAuthorize("hasPermission(null, 'role:view')")
    Response<List<Role>> findByUserId(@PathVariable String userId);

}
