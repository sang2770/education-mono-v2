package com.sang.nv.education.notification.infrastructure.persistence.mapper;


import com.sang.commonmodel.mapper.EntityMapper;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.infrastructure.persistence.entity.EventEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventEntityMapper extends EntityMapper<Event, EventEntity> {
}
