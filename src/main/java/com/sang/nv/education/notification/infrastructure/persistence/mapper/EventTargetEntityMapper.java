package com.sang.nv.education.notification.infrastructure.persistence.mapper;


import com.sang.commonmodel.mapper.EntityMapper;
import com.sang.nv.education.notification.domain.EventTarget;
import com.sang.nv.education.notification.infrastructure.persistence.entity.EventTargetEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EventTargetEntityMapper extends EntityMapper<EventTarget, EventTargetEntity> {
}
