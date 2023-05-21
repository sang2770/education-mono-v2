package com.sang.nv.education.notification.infrastructure.persistence.entity;

import com.sang.commonmodel.entity.AuditableEntity;
import com.sang.commonmodel.validator.ValidateConstraint;
import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "notification_templates", indexes = {
        @Index(name = "notification_templates_deleted_idx", columnList = "deleted")
})
public class NotificationTemplateEntity extends AuditableEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Version
    @Column(name = "version")
    private Long version;

    @Column(name = "code", length = ValidateConstraint.LENGTH.CODE_MAX_LENGTH)
    @Enumerated(EnumType.STRING)
    private NotificationCode code;
}
