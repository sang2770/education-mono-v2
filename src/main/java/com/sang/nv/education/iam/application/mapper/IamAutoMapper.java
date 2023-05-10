package com.sang.nv.education.iam.application.mapper;


import com.sang.commonmodel.mapper.BaseAutoMapper;
import com.sang.nv.education.iam.application.dto.request.Classes.ClassesCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.DepartmentCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.KeyCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.Permission.PermissionUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.Role.RoleCreateOrUpdateRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserCreateRequest;
import com.sang.nv.education.iam.application.dto.request.User.UserUpdateRequest;
import com.sang.nv.education.iam.domain.command.ClassesCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.command.DepartmentCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.command.KeyCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.command.PermissionUpdateCmd;
import com.sang.nv.education.iam.domain.command.RoleCreateOrUpdateCmd;
import com.sang.nv.education.iam.domain.command.UserCreateOrUpdateCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IamAutoMapper extends BaseAutoMapper {
    DepartmentCreateOrUpdateCmd from(DepartmentCreateOrUpdateRequest request);

    ClassesCreateOrUpdateCmd from(ClassesCreateOrUpdateRequest request);

    KeyCreateOrUpdateCmd from(KeyCreateOrUpdateRequest request);

    RoleCreateOrUpdateCmd from(RoleCreateOrUpdateRequest request);

    UserCreateOrUpdateCmd from(UserUpdateRequest request);

    UserCreateOrUpdateCmd from(UserCreateRequest request);

    PermissionUpdateCmd from(PermissionUpdateRequest request);

}
