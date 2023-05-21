package com.sang.nv.education.notification.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import com.sang.nv.education.notification.infrastructure.support.enums.EventStatus;
import com.sang.nv.education.notification.infrastructure.support.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "events", indexes = {
        @Index(name = "events_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class EventEntity extends AuditableEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "effect_type", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH)
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(name = "status", length = ValidateConstraint.LENGTH.ENUM_MAX_LENGTH, nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    @Column(name = "attached_link", length = ValidateConstraint.LENGTH.VALUE_MAX_LENGTH)
    private String attachedLink;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "failure_causes", length = ValidateConstraint.LENGTH.NOTE_MAX_LENGTH)
    private String failureCauses;

    @Column(name = "deleted")
    protected Boolean deleted;
}
