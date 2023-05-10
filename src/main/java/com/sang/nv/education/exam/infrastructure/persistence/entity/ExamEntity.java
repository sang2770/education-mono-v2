package com.sang.nv.education.exam.infrastructure.persistence.entity;

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
@Table(name = "exams", indexes = {
        @Index(name = "exams_periodId_idx", columnList = "periodId"),
        @Index(name = "exams_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ExamEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "totalPoint", nullable = false)
    Float totalPoint;
    @Column(name = "numberQuestion", nullable = false)
    Integer numberQuestion;
    @Column(name = "periodId", nullable = false)
    String periodId;

    @Column(name = "periodName")
    String periodName;
    @Column(name = "subjectId", nullable = false)
    String subjectId;
    @Column(name = "subjectName")
    String subjectName;

    @Column(name = "code")
    String code;
    @Column(name = "time", nullable = false)
    Long time;

    @Column(name = "timeDelay", nullable = false)
    Long timeDelay;

    @Column(name = "name", nullable = false)
    String name;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExamEntity that = (ExamEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

