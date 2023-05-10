package com.sang.nv.education.exam.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.enums.UserType;
import com.sang.commonmodel.validator.ValidateConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "user_rooms", indexes = {
        @Index(name = "user_rooms_id_idx", columnList = "roomId"),
        @Index(name = "user_rooms_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserRoomEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "userId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String userId;

    @Column(name = "user_type", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    @Enumerated(EnumType.STRING)
    private UserType userType;
    @Column(name = "roomId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    String roomId;

    @Column(name = "username")
    String username;

    @Column(name = "fullName")
    String fullName;

    @Column(name = "code")
    String code;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserRoomEntity that = (UserRoomEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

