package com.sang.nv.education.exam.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewFileType;
import com.sang.nv.education.exam.infrastructure.support.enums.ExamReviewStatus;
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


    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    ExamReviewFileType type;

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

