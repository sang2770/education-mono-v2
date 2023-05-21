package com.sang.nv.education.notification.infrastructure.domainrepository;

import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.domain.repository.EventDomainRepository;
import com.sang.nv.education.notification.infrastructure.persistence.entity.EventEntity;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.EventEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.repository.EventEntityRepository;
import com.sang.nv.education.notification.infrastructure.persistence.repository.NotificationEntityRepository;
import com.sang.nv.education.notification.infrastructure.support.BadRequestError;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class EventDomainRepositoryImpl extends AbstractDomainRepository<Event, EventEntity, String> implements EventDomainRepository {
    private final EventEntityRepository eventEntityRepository;
    private final EventEntityMapper eventEntityMapper;

    private final NotificationEntityRepository notificationEntityRepository;
    private final NotificationEntityMapper notificationEntityMapper;

    public EventDomainRepositoryImpl(EventEntityRepository eventEntityRepository, EventEntityMapper eventEntityMapper, NotificationEntityRepository notificationEntityRepository, NotificationEntityMapper notificationEntityMapper) {
        super(eventEntityRepository, eventEntityMapper);
        this.eventEntityRepository = eventEntityRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.notificationEntityRepository = notificationEntityRepository;
        this.notificationEntityMapper = notificationEntityMapper;
    }

    @Override
    public Event getById(String id) {
        return this.findById(id).orElseThrow(() -> new ResponseException(BadRequestError.EVENT_NOT_FOUND));
    }

    @Override
    public Event save(Event domain) {
        if (!CollectionUtils.isEmpty(domain.getNotifications()))
        {
            this.notificationEntityRepository.saveAll(this.notificationEntityMapper.toEntity(domain.getNotifications()));
        }
        return domain;
    }
}
