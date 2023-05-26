package com.sang.nv.education.notification.application.mapper;

import com.sang.nv.education.notification.application.dto.request.IssueEventRequest;
import com.sang.nv.education.notification.domain.command.EventCmd;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationAutoMapper {

//    EventCreateCommand from(EventCreateRequest request);
//
//    EventUpdateCommand from(EventUpdateRequest request);
//
    EventCmd from(IssueEventRequest request);
//
//    DeviceCreateOrUpdateCmd from(DeviceCreateRequest request);
//
//    EventConfigurationUpdateCmd from(EventConfigurationUpdateRequest request);
//
//    NotificationTemplateCreateCmd from(NotificationTemplateCreateRequest request);
//
//    NotificationTemplateUpdateCmd from(NotificationTemplateUpdateRequest request);
//
//    List<EventDTO> fromEvent(List<Event> domains);
//
//    List<EventConfigurationDTO> fromEventConfiguration(List<EventConfiguration> domains);

}
