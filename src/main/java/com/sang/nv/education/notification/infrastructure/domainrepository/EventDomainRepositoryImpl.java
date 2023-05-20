package com.sang.nv.education.notification.infrastructure.domainrepository;

import com.sang.nv.education.common.web.support.AbstractDomainRepository;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.domain.repository.EventDomainRepository;
import com.sang.nv.education.notification.infrastructure.persistence.entity.EventEntity;
import org.springframework.stereotype.Service;

@Service
public class EventDomainRepositoryImpl extends AbstractDomainRepository<Event, EventEntity, String> implements EventDomainRepository {
//    private final EventEntityRepository eventEntityRepository;
//
//    public EventDomainRepositoryImpl(EventEntityRepository eventEntityRepository) {
//        super(eventEntityRepository, fileEntityMapper);
//        this.eventEntityRepository = eventEntityRepository;
//        this.fileEntityMapper = fileEntityMapper;
//    }
//
//    @Override
//    public Event getById(String id) {
//        return this.findById(id).orElseThrow(() -> new ResponseException(BadRequestError.FILE_NOT_FOUND));
//    }
}
