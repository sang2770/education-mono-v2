package com.sang.nv.education.notification.infrastructure.persistence.mapper;


import com.sang.commonmodel.mapper.EntityMapper;
import com.sang.nv.education.notification.domain.Notification;
import com.sang.nv.education.notification.infrastructure.persistence.entity.NotificationEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationEntityMapper extends EntityMapper<Notification, NotificationEntity> {
}
