package com.sang.nv.education.iam.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "department", indexes = {
        @Index(name = "department_name_idx", columnList = "name"),
        @Index(name = "department_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class DepartmentEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "name", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String name;

    @Column(name = "phone", length = ValidateConstraint.LENGTH.PHONE_MAX_LENGTH, nullable = false)
    String phone;

    @Column(name = "address", length = ValidateConstraint.LENGTH.ADDRESS_MAX_LENGTH, nullable = false)
    String address;
    @Column(name = "code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH)
    String code;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DepartmentEntity that = (DepartmentEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
