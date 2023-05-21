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
    @JsonIgnore
    private List<Notification> notifications;

    public Event(EventCmd cmd, List<User> users) {
        this.id = IdUtils.nextId();
        this.title = cmd.getTitle();
        this.content = cmd.getContent();
        this.eventType = cmd.getEventType();
        this.status = EventStatus.IN_PROGRESS;
        this.attachedLink = cmd.getAttachedLink();
        if (!CollectionUtils.isEmpty(users)) {
            this.notifications = this.createNotifications(users);
        }
    }

    private List<Notification> createNotifications(List<User> users) {
        List<Notification> notifications = new ArrayList<>();
        users.forEach(user -> {
            notifications.add(new Notification(user.getId(), this.id));
        });
        return notifications;
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

}
