package com.sang.nv.education.notification.infrastructure.domainrepository;

import com.sang.commonmodel.exception.ResponseException;
import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.domain.EventTarget;
import com.sang.nv.education.notification.domain.repository.EventDomainRepository;
import com.sang.nv.education.notification.infrastructure.persistence.entity.EventEntity;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.EventEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.EventTargetEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.mapper.NotificationEntityMapper;
import com.sang.nv.education.notification.infrastructure.persistence.repository.EventEntityRepository;
import com.sang.nv.education.notification.infrastructure.persistence.repository.EventTargetEntityRepository;
import com.sang.nv.education.notification.infrastructure.persistence.repository.NotificationEntityRepository;
import com.sang.nv.education.notification.infrastructure.support.enums.BadRequestError;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class EventDomainRepositoryImpl extends AbstractDomainRepository<Event, EventEntity, String> implements EventDomainRepository {
    private final EventEntityRepository eventEntityRepository;
    private final EventEntityMapper eventEntityMapper;

    private final NotificationEntityRepository notificationEntityRepository;
    private final NotificationEntityMapper notificationEntityMapper;
    private final EventTargetEntityRepository eventTargetEntityRepository;
    private final EventTargetEntityMapper eventTargetEntityMapper;

    public EventDomainRepositoryImpl(EventEntityRepository eventEntityRepository, EventEntityMapper eventEntityMapper, NotificationEntityRepository notificationEntityRepository, NotificationEntityMapper notificationEntityMapper, EventTargetEntityRepository eventTargetEntityRepository, EventTargetEntityMapper eventTargetEntityMapper) {
        super(eventEntityRepository, eventEntityMapper);
        this.eventEntityRepository = eventEntityRepository;
        this.eventEntityMapper = eventEntityMapper;
        this.notificationEntityRepository = notificationEntityRepository;
        this.notificationEntityMapper = notificationEntityMapper;
        this.eventTargetEntityRepository = eventTargetEntityRepository;
        this.eventTargetEntityMapper = eventTargetEntityMapper;
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
        if (!CollectionUtils.isEmpty(domain.getEventTargets()))
        {
            this.eventTargetEntityRepository.saveAll(this.eventTargetEntityMapper.toEntity(domain.getEventTargets()));
        }
        this.eventEntityRepository.save(this.eventEntityMapper.toEntity(domain));
        return domain;
    }

    @Override
    protected Event enrich(Event event) {
        List<EventTarget> eventTargetList = this.eventTargetEntityMapper.toDomain(this.eventTargetEntityRepository.findAllByEventId(event.getId()));
        if (!CollectionUtils.isEmpty(eventTargetList)){
            event.enrichEventTarget(eventTargetList);
        }
        return event;
    }
}
