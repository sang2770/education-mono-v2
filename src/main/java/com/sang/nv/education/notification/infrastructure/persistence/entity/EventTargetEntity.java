package com.sang.nv.education.notification.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "event_targets", indexes = {
        @Index(name = "event_target_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EventTargetEntity extends AuditableEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "event_Id", nullable = false)
    private String eventId;

    @Column(name = "target_Id", nullable = false)
    private String targetId;

}
