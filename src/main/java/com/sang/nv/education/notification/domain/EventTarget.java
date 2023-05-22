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

@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class EventTarget extends AuditableDomain {

    private String id;

    private String eventId;

    private String targetId;

    private String email;

    private Boolean deleted;

    public EventTarget(String eventId, String targetId) {
        this.id = IdUtils.nextId();
        this.eventId = eventId;
        this.targetId = targetId;
        this.deleted = false;
    }

    public EventTarget(String eventId, String targetId, String email) {
        this.id = IdUtils.nextId();
        this.eventId = eventId;
        this.targetId = targetId;
        this.email = email;
        this.deleted = false;
    }

    public void delete() {
        this.deleted = true;
    }

    public void unDeleted() {
        this.deleted = false;
    }
}
