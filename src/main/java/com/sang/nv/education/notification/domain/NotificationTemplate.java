package com.sang.nv.education.notification.domain;

import com.sang.commonmodel.domain.AuditableDomain;
import com.sang.commonutil.IdUtils;
import com.sang.nv.education.notification.domain.command.NotificationTemplateCreateCmd;
import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;
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
public class NotificationTemplate extends AuditableDomain {
    private String id;
    private NotificationCode code;
    private String title;
    private String content;
    private String url;
    private Long version;
    public NotificationTemplate(NotificationTemplateCreateCmd cmd) {
        this.id = IdUtils.nextId();
        this.code = cmd.getCode();
        this.title = cmd.getTitle();
        this.content = cmd.getContent();
        this.deleted = Boolean.FALSE;
        this.url = cmd.getUrl();
    }
}
