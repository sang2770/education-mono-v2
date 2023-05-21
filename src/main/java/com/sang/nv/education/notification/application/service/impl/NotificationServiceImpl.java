package com.sang.nv.education.notification.application.service.impl;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.nv.education.notification.application.dto.request.NotificationMarkReadRequest;
import com.sang.nv.education.notification.application.dto.request.NotificationSearchRequest;
import com.sang.nv.education.notification.application.mapper.NotificationAutoMapperQuery;
import com.sang.nv.education.notification.application.service.NotificationService;
import com.sang.nv.education.notification.domain.Notification;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.NotificationEntityMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationEntityMapper notificationEntityMapper;
    private final NotificationAutoMapperQuery notificationAutoMapperQuery;
    @Override
    public Notification ensureExisted(String uuid) {
        return null;
    }

    @Override
    public Notification getNotificationByEventId(String eventId) {
        return null;
    }

    @Override
    public Notification getNotificationById(String id) {
        return null;
    }

    @Override
    public PageDTO<Notification> findAllByUserLogin(NotificationSearchRequest params) {
        return null;
    }

    @Override
    public Boolean markAllRead(NotificationMarkReadRequest notificationMarkReadRequest) {
        return null;
    }

    @Override
    public Boolean markRead(String id) {
        return null;
    }

    @Override
    public Long countUnreadNotification() {
        return null;
    }

    @Override
    public Boolean markReadAll() {
        return null;
    }
}
