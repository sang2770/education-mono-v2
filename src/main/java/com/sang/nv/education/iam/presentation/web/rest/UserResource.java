package com.sang.nv.education.iam.presentation.web.rest;


import com.sang.commonmodel.auth.UserAuthority;
import com.sang.commonmodel.dto.request.FindByIdsRequest;
import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.commonmodel.validator.anotations.ValidatePaging;
import com.sang.nv.education.iam.application.dto.request.User.ChangePasswordRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserCreateRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserExportRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserSearchRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserUpdateRoleRequest;
import com.sang.nv.education.iam.application.dto.response.ImportResult;
import com.sang.nv.education.iam.domain.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "User Resource")
@RequestMapping("/api")
public interface UserResource {
    @ApiOperation(value = "Create User")
    @PostMapping("/users")
    Response<User> create(@RequestBody UserCreateRequest request);

    @ApiOperation(value = "Update user")
    @PostMapping("/users/{id}/update")
    Response<User> update(@PathVariable String id, @RequestBody @Valid UserUpdateRequest request);

    @ApiOperation(value = "Get user by Id")
    @GetMapping("/users/{id}")
    Response<User> getUserById(@PathVariable String id);

    @ApiOperation(value = "Delete user")
    @PostMapping("/users/{id}/delete")
    Response<Void> delete(@PathVariable String id);


    @ApiOperation(value = "Search user")
    @GetMapping("/users")
    PagingResponse<User> search(@ValidatePaging(allowedSorts = {"lastModifiedAt", "createdAt",
            "fullName", "username", "email", "status", "userType"})
                                UserSearchRequest request);


    @ApiOperation(value = "Active User")
    @PostMapping("/users/{userId}/active")
    Response<Boolean> active(@PathVariable String userId);

    @ApiOperation(value = "Inactive User")
    @PostMapping("/users/{userId}/inactive")
    Response<Boolean> inactive(@PathVariable String userId);

    @ApiOperation(value = "Change password internal user")
    @PostMapping("/users/{userId}/change-password")
    Response<User> changePassword(@PathVariable String userId, @RequestBody @Valid ChangePasswordRequest request);

    @ApiOperation(value = "Search User auto complete")
    @GetMapping("/users/auto-complete")
    PagingResponse<User> autocomplete(@ValidatePaging(allowedSorts = {"lastModifiedAt", "createdAt", "employeeCode", "fullName", "username", "email"})
                                      UserSearchRequest request);

    // ************************************* API FOR CLIENT **************************************

    @ApiOperation(value = "Get my authorities")
    @GetMapping("/users/{userId}/authorities")
    Response<UserAuthority> getAuthoritiesByUserId(@PathVariable String userId);


    //use for both client and user
    @ApiOperation(value = "Find user by ids")
    @PostMapping("/users/find-by-ids")
    Response<List<User>> findByIds(@RequestBody FindByIdsRequest request);


    @ApiOperation(value = "Export users")
    @GetMapping("/users/exports")
    void exportUsers(@Valid UserExportRequest request, HttpServletResponse response);

    @ApiOperation(value = "Download template import")
    @GetMapping("/users/download-template")
    void downloadTemplate(HttpServletResponse response);

    @ApiOperation(value = "Import users")
    @PostMapping("/users/import")
    Response<ImportResult> importUsers(@RequestParam("file") MultipartFile file,
                                       HttpServletResponse response);

    @ApiOperation(value = "Update Role User")
    @PostMapping("/users/{id}/update-roles")
    Response<User> updateRole(@PathVariable String id, @RequestBody UserUpdateRoleRequest request);


}
