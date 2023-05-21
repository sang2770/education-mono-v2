package com.sang.nv.education.notification.infrastructure.support.enums;

import lombok.Getter;

@Getter
public enum NotificationTemplateResource {
    ROOM_IN(NotificationCode.ROOM_IN, "Bạn có phòng thi mới", "Bạn đã được phân vào phòng thi %s bởi %s", "/rooms/%s/detail"),
    NEW_EXAM(NotificationCode.NEW_EXAM, "Bạn có đề thi mới", "Bạn có bài thi mới tại kỳ thi %s tại phòng thi %s", "/rooms/%s/periods/%s"),
    ;

    NotificationTemplateResource(NotificationCode code, String title, String content, String url) {
        this.code = code;
        this.title = title;
        this.content = content;
        this.url = url;
    }
    NotificationCode code;
    String title;
    String content;
    String url;
}
