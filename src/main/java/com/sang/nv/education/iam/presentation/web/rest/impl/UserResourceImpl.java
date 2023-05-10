package com.sang.nv.education.iam.presentation.web.rest.impl;

import com.sang.commonmodel.auth.UserAuthority;
import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.common.web.security.AuthorityService;
import com.sang.nv.education.iam.application.dto.request.User.ChangePasswordRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserCreateRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserExportRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserSearchRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserUpdateRoleRequest;
import com.sang.nv.education.iam.application.dto.response.ImportResult;
import com.sang.nv.education.iam.application.service.UserService;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.iam.presentation.web.rest.UserResource;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class UserResourceImpl implements UserResource {

    private final UserService userService;
    private final AuthorityService authorityService;

    public UserResourceImpl(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }

    @Override
    public Response<User> create(UserCreateRequest request) {
        return Response.of(this.userService.create(request));
    }

    @Override
    public Response<User> update(String id, UserUpdateRequest request) {
        return Response.of(this.userService.update(id, request));
    }

    @Override
    public Response<User> getUserById(String id) {
        return Response.of(this.userService.getUserById(id));
    }

    @Override
    public Response<Void> delete(String id) {
        this.userService.delete(id);
        return Response.ok();
    }

    @Override
    public PagingResponse<User> search(UserSearchRequest request) {
        return PagingResponse.of(this.userService.search(request));
    }

    @Override
    public Response<Boolean> active(String userId) {
        this.userService.active(userId);
        return Response.ok();
    }

    @Override
    public Response<Boolean> inactive(String userId) {
        this.userService.inactive(userId);
        return Response.ok();
    }

    @Override
    public Response<User> changePassword(String userId, ChangePasswordRequest request) {
        return Response.of(this.userService.changePassword(userId, request));
    }

    @Override
    public PagingResponse<User> autocomplete(UserSearchRequest request) {
        return null;
    }

    @Override
    public Response<UserAuthority> getAuthoritiesByUserId(String userId) {
        UserAuthority userAuthority = authorityService.getUserAuthority(userId);
        return Response.of(userAuthority);
    }

    @Override
    public Response<List<User>> findByIds(FindByIdsRequest request) {
        return Response.of(this.userService.findByIds(request.getIds()));
    }

    @Override
    public void exportUsers(UserExportRequest request, HttpServletResponse response) {
        this.userService.exportUsers(request, response);
    }

    @Override
    public void downloadTemplate(HttpServletResponse response) {
        this.userService.downloadTemplateImportUsers(response);
    }

    @Override
    public Response<ImportResult> importUsers(MultipartFile file, HttpServletResponse response) {
        return Response.of(userService.importUser(file, response));
    }

    @Override
    public Response<User> updateRole(String id, UserUpdateRoleRequest request) {
        return Response.of(this.userService.updateRole(id, request));
    }
}
