package com.sang.nv.education.exam.application.service.impl;

import com.sang.nv.education.exam.application.service.SendNotificationService;
import com.sang.nv.education.notification.application.service.NotificationTemplateService;
import com.sang.nv.education.notification.domain.NotificationTemplate;
import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SendNotificationServiceImpl implements SendNotificationService {
    @Value("${app.iam.domain}")
    private String domain;

    private final NotificationTemplateService notificationTemplateService;

    public SendNotificationServiceImpl(NotificationTemplateService notificationTemplateService) {
        this.notificationTemplateService = notificationTemplateService;
    }

    @Override
    public void sendNotification(NotificationCode notificationCode, Map<String, String> extraData) {
        NotificationTemplate notificationTemplate = this.notificationTemplateService.getByCode(notificationCode);
    }

    private String getAttackedLink(NotificationCode)
}
