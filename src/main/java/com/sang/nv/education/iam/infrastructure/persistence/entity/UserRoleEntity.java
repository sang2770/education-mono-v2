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
@Table(name = "user_role", indexes = {
        @Index(name = "user_role_user_id_idx", columnList = "user_id"),
        @Index(name = "user_role_role_id_idx", columnList = "role_id"),
        @Index(name = "user_role_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRoleEntity extends AuditableEntity {

    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "user_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String userId;

    @Column(name = "role_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String roleId;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserRoleEntity that = (UserRoleEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
