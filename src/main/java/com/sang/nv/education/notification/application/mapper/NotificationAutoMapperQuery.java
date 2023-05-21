package com.sang.nv.education.notification.application.mapper;


import com.sang.nv.education.notification.application.dto.request.NotificationSearchRequest;
import com.sang.nv.education.notification.domain.query.NotificationSearchQuery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationAutoMapperQuery {
    NotificationSearchQuery toQuery(NotificationSearchRequest notificationSearchRequest);
}
