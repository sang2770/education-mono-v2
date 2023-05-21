package com.sang.nv.education.notification.application.service;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.nv.education.notification.application.dto.request.NotificationMarkReadRequest;
import com.sang.nv.education.notification.application.dto.request.NotificationSearchRequest;
import com.sang.nv.education.notification.domain.Notification;

public interface NotificationService {

    Notification ensureExisted(String uuid);

    Notification getNotificationByEventId(String eventId);

    Notification getNotificationById(String id);

    PageDTO<Notification> findAllByUserLogin(NotificationSearchRequest params);

    Boolean markAllRead(NotificationMarkReadRequest notificationMarkReadRequest);

    Boolean markRead(String id);

    Long countUnreadNotification();

    Boolean markReadAll();

}
