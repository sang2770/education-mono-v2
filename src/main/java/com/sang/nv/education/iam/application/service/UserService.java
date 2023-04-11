package com.sang.nv.education.iam.application.service;


import com.sang.commonmodel.dto.PageDTO;
import com.sang.nv.education.iamapplication.dto.response.ImportResult;
import com.sang.nv.education.iamdomain.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {
    User ensureExisted(String id);


    User create(UserCreateRequest request);


    User update(String userId, UserUpdateRequest request);

    void delete(String id);

    PageDTO<User> search(UserSearchRequest request);

    void deleteByIds(List<String> ids);

    List<User> findByIds(List<String> ids);

    void active(String userId);

    void inactive(String userId);

    User getUserById(String userId);

    User changePassword(String userId, UserChangePasswordRequest request);


    PageDTO<User> autoComplete(UserSearchRequest request);

    void exportUsers(UserExportRequest request, HttpServletResponse response);
    void downloadTemplateImportUsers(HttpServletResponse response);

    ImportResult importUser(MultipartFile file, HttpServletResponse response);
}
