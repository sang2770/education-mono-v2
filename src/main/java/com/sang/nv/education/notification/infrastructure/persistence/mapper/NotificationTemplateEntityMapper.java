package com.sang.nv.education.notification.infrastructure.persistence.mapper;


import com.sang.commonmodel.mapper.EntityMapper;
import com.sang.nv.education.notification.domain.NotificationTemplate;
import com.sang.nv.education.notification.infrastructure.persistence.entity.NotificationTemplateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationTemplateEntityMapper extends EntityMapper<NotificationTemplate, NotificationTemplateEntity> {
}
