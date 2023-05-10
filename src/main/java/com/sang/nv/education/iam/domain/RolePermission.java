package com.sang.nv.education.iam.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonmodel.enums.Scope;
import com.sang.commonutil.IdUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class RolePermission extends AuditableDomain {

    @JsonIgnore
    private String id;
    private String roleId;
    private String resourceCode;
    private Scope scope;
    private Boolean deleted;

    public RolePermission(String roleId, String resourceCode, Scope scope) {
        this.id = IdUtils.nextId();
        this.roleId = roleId;
        this.resourceCode = resourceCode;
        this.scope = scope;
        this.deleted = false;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }
}
