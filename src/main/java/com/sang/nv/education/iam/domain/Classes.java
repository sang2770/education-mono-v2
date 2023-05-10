package com.sang.nv.education.iam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.iam.domain.command.ClassesCreateOrUpdateCmd;
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
public class Classes extends AuditableDomain {
    String id;
    String name;
    String keyId;
    String code;
    String departmentId;
    Boolean deleted;

    Key key;
    Department department;

    public Classes(ClassesCreateOrUpdateCmd cmd) {
        this.id = IdUtils.nextId();
        this.name = cmd.getName();
        this.keyId = cmd.getKeyId();
        this.departmentId = cmd.getDepartmentId();
        this.code = cmd.getCode();
        this.deleted = Boolean.FALSE;
    }

    public void update(ClassesCreateOrUpdateCmd cmd) {
        this.name = cmd.getName();
        this.keyId = cmd.getKeyId();
//        this.code = cmd.getCode();
        this.deleted = Boolean.FALSE;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void enrichKey(Key key) {
        this.key = key;
    }

    public void enrichDepartment(Department department) {
        this.department = department;
    }
}
