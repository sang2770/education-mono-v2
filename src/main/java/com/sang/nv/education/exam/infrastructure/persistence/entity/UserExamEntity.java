package com.sang.nv.education.exam.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import com.sang.nv.education.exam.infrastructure.support.enums.UserExamStatus;
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
import java.time.Instant;
import java.util.Objects;

//Bài thi
@Entity
@Table(name = "user_exams", indexes = {
        @Index(name = "user_exams_examId", columnList = "examId"),
        @Index(name = "user_exams_roomId", columnList = "roomId")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserExamEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "code")
    String code;

    @Column(name = "totalPoint")
    Float totalPoint;

    @Column(name = "maxPoint")
    Float maxPoint;

    @Column(name = "timeEnd")
    Instant timeEnd;

    @Column(name = "timeStart")
    Instant timeStart;

    @Column(name = "timeDelay")
    Long timeDelay;

    @Column(name = "examId")
    String examId;

    @Column(name = "userId")
    String userId;

    @Column(name = "numberOutTab")
    Integer numberOutTab;

    @Column(name = "periodId")
    String periodId;

    @Column(name = "roomId")
    String roomId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    UserExamStatus status;

    @Column(name = "deleted")
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserExamEntity that = (UserExamEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

