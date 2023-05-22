package com.sang.nv.education.notification.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.notification.domain.command.EventCmd;
import com.sang.nv.education.notification.infrastructure.support.enums.EventStatus;
import com.sang.nv.education.notification.infrastructure.support.enums.EventType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter(AccessLevel.PRIVATE)
@Getter
public class Event extends AuditableDomain {
    private String id;
    private String title;
    private String content;
    private EventType eventType;
    private EventStatus status;
    private String attachedLink;
    private Long version;
    private String failureCauses;
    private List<EventTarget> eventTargets;
    private Boolean deleted;
    @JsonIgnore
    private List<Notification> notifications;

    public Event(EventCmd cmd, List<User> users) {
        this.id = IdUtils.nextId();
        this.title = cmd.getTitle();
        this.content = cmd.getContent();
        this.eventType = cmd.getEventType();
        this.status = EventStatus.IN_PROGRESS;
        this.attachedLink = cmd.getAttachedLink();
        this.deleted = false;
        if (!CollectionUtils.isEmpty(users)) {
            this.eventTargets = this.createTargets(users);
        }
    }

    private List<EventTarget> createTargets(List<User> users) {
        List<EventTarget> eventTargetList = new ArrayList<>();
        users.forEach(user -> {
            eventTargetList.add(new EventTarget(this.id, user.getId(), user.getEmail()));
        });
        return eventTargetList;
    }

    public void delete() {
        this.deleted = true;
    }

    public void unDelete() {
        this.deleted = false;
    }

    public void retrySent() {
        this.status = EventStatus.IN_PROGRESS;
    }

    public void enrichEventTarget(List<EventTarget> eventTargets) {
        this.eventTargets = eventTargets;
    }

    public void sendNotification() {
        this.status = EventStatus.DONE;
        if (!CollectionUtils.isEmpty(this.getEventTargets())) {
            this.notifications = new ArrayList<>();
            this.getEventTargets().forEach(eventTarget -> {
                this.notifications.add(
                        new Notification(eventTarget.getTargetId(), this));
            });
        }
    }
}
