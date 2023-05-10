package com.sang.nv.education.iam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
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
public class UserRole extends AuditableDomain {

    private String id;
    private String userId;
    private String roleId;
    private Boolean deleted;

    public UserRole(String userId, String roleId) {
        this.id = IdUtils.nextId();
        this.userId = userId;
        this.roleId = roleId;
        this.deleted = false;
    }

    void deleted() {
        this.deleted = true;
    }

    void unDelete() {
        this.deleted = false;
    }
}
