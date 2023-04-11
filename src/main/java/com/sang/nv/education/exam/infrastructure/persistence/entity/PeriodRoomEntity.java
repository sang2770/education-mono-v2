package com.sang.nv.education.exam.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "period_rooms", indexes = {
        @Index(name = "period_rooms_id_idx", columnList = "roomId"),
        @Index(name = "period_rooms_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class PeriodRoomEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "periodId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String periodId;
    @Column(name = "roomId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    String roomId;
    @Column(name = "deleted", nullable = false)
    private Boolean deleted;
    @Column(name = "is_send_exam")
    private Boolean isSendExam;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PeriodRoomEntity that = (PeriodRoomEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

