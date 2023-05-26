package com.sang.nv.education.notification.application.service.impl;

import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.notification.application.service.NotificationTemplateService;
import com.sang.nv.education.notification.domain.NotificationTemplate;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.NotificationTemplateEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.repository.NotificationTemplateEntityRepository;
import com.sang.nv.education.notification.infrastructure.support.enums.NotFoundError;
import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationTemplateServiceImpl implements NotificationTemplateService {
    private final NotificationTemplateEntityRepository notificationTemplateEntityRepository;
    private final NotificationTemplateEntityMapper notificationTemplateEntityMapper;
    @Override
    public NotificationTemplate getByCode(NotificationCode notificationCode) {
        return this.notificationTemplateEntityRepository.findById(notificationCode).map(this.notificationTemplateEntityMapper::toDomain)
                .orElseThrow(() -> new ResponseException(NotFoundError.NOTIFICATION_NOT_FOUND));
    }
}
