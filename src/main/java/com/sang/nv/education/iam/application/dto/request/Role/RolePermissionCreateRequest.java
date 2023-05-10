package com.sang.nv.education.iam.application.dto.request.Role;

import com.sang.commonmodel.dto.request.Request;
import com.sang.commonmodel.enums.Scope;
import com.sang.commonmodel.validator.ValidateConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolePermissionCreateRequest extends Request {

    @Size(max = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, message = "ROLE_PERMISSION_RESOURCE_LENGTH")
    private String resourceCode;

    private List<Scope> scopes;
}
