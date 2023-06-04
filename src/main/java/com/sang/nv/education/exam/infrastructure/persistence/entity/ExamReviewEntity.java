package com.sang.nv.education.exam.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewStatus;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "exam_review", indexes = {@Index(name = "exam_review_user_exam_idx", columnList = "userExamId"), @Index(name = "exam_review_deleted_idx", columnList = "deleted")})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ExamReviewEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH, nullable = false)
    private String code;

    @Column(name = "userId")
    String userId;

    @Column(name = "examId")
    String examId;

    @Column(name = "roomId")
    String roomId;

    @Column(name = "periodId")
    String periodId;

    @Column(name = "feedBack")
    String feedBack;

    @Column(name = "userExamId")
    String userExamId;
    @Column(name = "userExamCode")
    String userExamCode;

    @Column(name = "timeCompletedAt")
    Instant timeCompletedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    ExamReviewStatus status;


    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExamReviewEntity that = (ExamReviewEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

