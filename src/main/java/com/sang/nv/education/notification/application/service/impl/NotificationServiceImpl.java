package com.sang.nv.education.notification.application.service.impl;

import com.sang.commonmodel.dto.PageDTO;
import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.SecurityUtils;
import com.sang.nv.education.notification.application.dto.request.NotificationMarkReadRequest;
import com.sang.nv.education.notification.application.dto.request.NotificationSearchRequest;
import com.sang.nv.education.notification.application.mapper.NotificationAutoMapperQuery;
import com.sang.nv.education.notification.application.service.NotificationService;
import com.sang.nv.education.notification.domain.Notification;
import com.sang.nv.education.notification.domain.query.NotificationSearchQuery;
import com.sang.nv.education.notification.infrastructure.persistence.entity.NotificationEntity;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.EventEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.repository.EventEntityRepository;
import com.sang.nv.education.notification.infrastructure.persistence.repository.NotificationEntityRepository;
import com.sang.nv.education.notification.infrastructure.support.enums.BadRequestError;
import com.sang.nv.education.notification.infrastructure.support.enums.NotFoundError;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationEntityMapper notificationEntityMapper;
    private final NotificationAutoMapperQuery notificationAutoMapperQuery;
    private final EventEntityMapper eventEntityMapper;
    private final EventEntityRepository eventRepository;
    private final NotificationEntityRepository notificationRepository;

    @Override
    public Notification ensureExisted(String uuid) {
        return this.notificationRepository.findById(uuid).map(this.notificationEntityMapper::toDomain)
                .orElseThrow(() -> new ResponseException(BadRequestError.NOTIFICATION_NOT_FOUND));
    }

    @Override
    public Notification getNotificationByEventId(String eventId) {
        return null;
    }

    @Override
    public Notification getNotificationById(String id) {
        return this.ensureExisted(id);
    }

    @Override
    public PageDTO<Notification> findAllByUserLogin(NotificationSearchRequest request) {
        NotificationSearchQuery params = notificationAutoMapperQuery.toQuery(request);
        params.setUserId(this.currentUserId());
        List<Notification> notifications = this.notificationEntityMapper.toDomain(notificationRepository.search(params));
        return new PageDTO<>(notifications, params.getPageIndex(), params.getPageSize(), notificationRepository.count(params));
    }

    @Override
    public Boolean markAllRead(NotificationMarkReadRequest notificationMarkReadRequest) {
        log.info("Mark all read by ids: {}", notificationMarkReadRequest.getIds());
        List<Notification> notifications = this.findAllNotificationByIdsAndCurrentUserId(notificationMarkReadRequest.getIds());
        notifications.forEach(Notification::read);
        this.notificationRepository.saveAll(this.notificationEntityMapper.toEntity(notifications));
        return true;
    }

    @Override
    public Boolean markRead(String id) {
        String currentUserId = currentUserId();
        log.info("User {} mark read by id: {}", currentUserId, id);
        NotificationEntity notificationEntity = notificationRepository.findByIdAndUserId(id, currentUserId)
                .orElseThrow(() -> new ResponseException(NotFoundError.NOTIFICATION_NOT_FOUND));
        Notification notification = notificationEntityMapper.toDomain(notificationEntity);
        notification.read();
        this.notificationRepository.save(this.notificationEntityMapper.toEntity(notification));
        return true;
    }

    @Override
    public Long countUnreadNotification() {
        return notificationRepository.countUnreadNotification(currentUserId());
    }

    @Override
    public Boolean markReadAll() {
        String currentUserId = currentUserId();
        log.info("Mark read all by user: {}", currentUserId);
        List<NotificationEntity> notificationEntities = notificationRepository.findAllUnreadNotiByUserId(currentUserId);
        if (CollectionUtils.isEmpty(notificationEntities)) {
            throw new ResponseException(NotFoundError.NOTIFICATION_NOT_FOUND);
        }
        List<Notification> notifications = notificationEntityMapper.toDomain(notificationEntities);
        notifications.forEach(Notification::read);
        this.notificationRepository.saveAll(this.notificationEntityMapper.toEntity(notifications));
        return true;
    }

    private String currentUserId() {
        return SecurityUtils.getCurrentUserLoginId().orElseThrow(() ->
                new ResponseException(NotFoundError.USER_NOT_FOUND));
    }

    private List<Notification> findAllNotificationByIdsAndCurrentUserId(List<String> ids) {
        List<NotificationEntity> notificationEntities = notificationRepository.findAllByIdsAndUserId(ids, this.currentUserId());
        if (CollectionUtils.isEmpty(notificationEntities)) {
            throw new ResponseException(NotFoundError.NOTIFICATION_NOT_FOUND);
        }
        return notificationEntityMapper.toDomain(notificationEntities);
    }
}
