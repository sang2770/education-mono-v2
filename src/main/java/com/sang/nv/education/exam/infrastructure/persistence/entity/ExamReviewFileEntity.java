package com.sang.nv.education.exam.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "exam_review_file", indexes = {
        @Index(name = "exam_review_file_idx", columnList = "fileId"),
        @Index(name = "exam_review_file_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ExamReviewFileEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;
    @Column(name = "examReviewId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String examReviewId;
    @Column(name = "fileId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String fileId;
    @Column(name = "viewUrl")
    String viewUrl;
    @Column(name = "fileName")
    String fileName;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExamReviewFileEntity that = (ExamReviewFileEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

