package com.sang.nv.education.notification.infrastructure.persistence.repository.impl;

import com.sang.commonpersistence.repository.BaseRepositoryImpl;
import com.sang.nv.education.notification.infrastructure.persistence.entity.EventEntity;
import org.springframework.stereotype.Repository;

@Repository
public class EventEntityRepositoryImpl extends BaseRepositoryImpl<EventEntity> {
    @Override
    public Class getClassName() {
        return EventEntity.class;
    }
}
