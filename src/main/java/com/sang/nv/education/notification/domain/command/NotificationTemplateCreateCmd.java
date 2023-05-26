package com.sang.nv.education.notification.domain.command;

import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplateCreateCmd {
    private NotificationCode code;
    private String title;
    private String content;
    private String url;
}
