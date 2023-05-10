package com.sang.nv.education.iam.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.iam.domain.command.DepartmentCreateOrUpdateCmd;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Department extends AuditableDomain {
    String id;
    String phone;
    String name;
    String address;
    String code;
    List<Classes> classes;
    Boolean deleted;

    public Department(DepartmentCreateOrUpdateCmd cmd) {
        this.id = IdUtils.nextId();
        this.phone = cmd.getPhone();
        this.name = cmd.getName();
        this.address = cmd.getAddress();
        this.deleted = Boolean.FALSE;
        this.code = cmd.getCode();
    }

    public void update(DepartmentCreateOrUpdateCmd cmd) {
        this.phone = cmd.getPhone();
        this.name = cmd.getName();
        this.address = cmd.getAddress();
        this.code = cmd.getCode();
    }

    public void enrichClasses(List<Classes> classes) {
        this.classes = classes;
    }
}
