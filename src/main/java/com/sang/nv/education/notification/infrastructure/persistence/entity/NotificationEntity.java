package com.sang.nv.education.notification.infrastructure.persistence.entity;

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
import javax.persistence.Version;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "notifications_event_idx", columnList = "eventId"),
        @Index(name = "notifications_deleted_idx", columnList = "deleted")
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NotificationEntity extends AuditableEntity {
    @Id
    @Column(name = "id", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String id;

    @Column(name = "userId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String userId;

    @Column(name = "eventId", length = ValidateConstraint.LENGTH.ID_MAX_LENGTH, nullable = false)
    private String eventId;

    @Column(name = "isRead", nullable = false)
    private Boolean isRead;

    @Column(name = "readAt")
    private Instant readAt;

    @Column(name = "isSend")
    private Boolean isSend;

    @Column(name = "sendAt")
    private Instant sendAt;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "content", length = ValidateConstraint.LENGTH.CONTENT_MAX_LENGTH, nullable = false)
    private String content;

    @Column(name = "title", length = ValidateConstraint.LENGTH.TITLE_MAX_LENGTH, nullable = false)
    private String title;

    @Column(name = "attachedLink", length = ValidateConstraint.LENGTH.VALUE_MAX_LENGTH, nullable = false)
    private String attachedLink;

    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        NotificationEntity that = (NotificationEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

