package com.sang.nv.education.notification.application.service;

import com.sang.nv.education.notification.domain.NotificationTemplate;
import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;

public interface NotificationTemplateService {

    NotificationTemplate getByCode(NotificationCode notificationCode);

}
