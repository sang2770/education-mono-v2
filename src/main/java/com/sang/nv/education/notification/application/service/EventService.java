package com.sang.nv.education.notification.application.service;


import com.sang.nv.education.notification.application.dto.request.IssueEventRequest;
import com.sang.nv.education.notification.domain.Event;

public interface EventService {
    Event issued(IssueEventRequest request);
    void send(String id);

}
