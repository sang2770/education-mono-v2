package com.sang.nv.education.exam.application.service;

import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;

import java.util.Map;

public interface SendNotificationService {
    void sendNotification(NotificationCode notificationCode, Map<String, String> extraData);
}
