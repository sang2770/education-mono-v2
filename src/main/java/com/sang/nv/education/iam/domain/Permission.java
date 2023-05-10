package com.sang.nv.education.iam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonmodel.enums.Scope;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.iam.domain.command.PermissionUpdateCmd;
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
public class Permission extends AuditableDomain {

    private String id;
    private Scope scope;
    private String resourceCode;
    private String resourceName;
    private String name;
    private Boolean deleted;
    private Integer priority;

    public Permission(String resourceCode, Scope scope, String name, Integer priority) {
        this.id = IdUtils.nextId();
        this.resourceCode = resourceCode;
        this.scope = scope;
        this.name = name;
        this.priority = priority;
        this.deleted = false;
    }

    public void update(PermissionUpdateCmd cmd) {
        this.name = cmd.getName();
    }


    public void deleted() {
        this.deleted = true;
    }

    public void enrichResourceName(String resourceName) {
        this.resourceName = resourceName;
    }
}
