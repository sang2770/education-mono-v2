package com.sang.nv.education.iam.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import com.sang.nv.education.iam.infrastructure.support.enums.UserStatus;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
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
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "user_username_idx", columnList = "username"),
        @Index(name = "user_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "username", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String username;

    @Column(name = "password", length = ValidateConstraint.LENGTH.VALUE_MAX_LENGTH, nullable = false)
    private String password;

    @Column(name = "full_name", length = ValidateConstraint.LENGTH.NAME_MAX_LENGTH, nullable = false)
    private String fullName;

    @Column(name = "email", length = ValidateConstraint.LENGTH.EMAIL_MAX_LENGTH, nullable = false)
    private String email;

    @Column(name = "phone_number", length = ValidateConstraint.LENGTH.PHONE_MAX_LENGTH)
    private String phoneNumber;

    @Column(name = "day_of_birth")
    private LocalDate dayOfBirth;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "isRoot")
    private Boolean isRoot;

    @Column(name = "code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH)
    private String code;

    @Column(name = "description", length = ValidateConstraint.LENGTH.DESC_MAX_LENGTH)
    private String description;

    @Column(name = "status", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "user_type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH)
    @Enumerated(EnumType.STRING)
    private UserType userType = UserType.STUDENT;

    @Column(name = "avatar_file_id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    private String avatarFileId;

    @Column(name = "avatar_file_view_url")
    private String avatarFileViewUrl;


    @Column(name = "classId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH)
    private String classId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserEntity that = (UserEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
