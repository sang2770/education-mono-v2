package com.sang.nv.education.notification.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Notification extends AuditableDomain {
    private String id;
    private String userId;
    private String eventId;
    private Boolean isRead;
    private Instant readAt;
    private Boolean isSend;
    private Instant sendAt;
    private Boolean deleted;
    private String content;
    private String title;
    private String attachedLink;
    private Long version;

    public Notification(String userId, String eventId) {
        this.id = IdUtils.nextId();
        this.userId = userId;
        this.eventId = eventId;
        this.isRead = false;
        this.isSend = false;
        this.deleted = false;
    }

    public void read() {
        this.isRead = true;
        this.readAt = Instant.now();
    }

    public void unread() {
        this.isRead = false;
        this.readAt = null;
    }

    public void sent() {
        this.isSend = true;
        this.sendAt = Instant.now();
    }

    public void deleted() {
        this.deleted = true;
    }

    public void enrichEvent(Event event) {
        this.title = event.getTitle();
        this.content = event.getContent();
        this.attachedLink = event.getAttachedLink();
    }
    public void failed() {
        this.isSend = false;
        this.sendAt = null;
    }
}
