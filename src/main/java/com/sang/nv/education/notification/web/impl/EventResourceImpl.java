package com.sang.nv.education.notification.web.impl;


import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.notification.application.dto.request.IssueEventRequest;
import com.sang.nv.education.notification.application.service.EventService;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.web.EventResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class EventResourceImpl implements EventResource {

    private final EventService eventService;

    @Override
    public Response<Event> issuedEvent(IssueEventRequest issueEventRequest) {
        return Response.of(this.eventService.issued(issueEventRequest));
    }

    @Override
    public Response<Boolean> send(String id) {
        this.eventService.send(id);
        return Response.ok();
    }
}
