package com.sang.nv.education.notification.web;

import com.sang.commonmodel.dto.response.Response;
import com.sang.commonmodel.validator.anotations.ValidatePaging;
import com.sang.nv.education.notification.application.dto.request.IssueEventRequest;
import com.sang.nv.education.notification.domain.Event;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = "Event resource")
@RequestMapping("/api")
public interface EventResource {
    @ApiOperation(value = "Send issued")
    @PostMapping("/event/issue")
    Response<Event> issuedEvent(@ValidatePaging IssueEventRequest issueEventRequest);

    @ApiOperation(value = "Send event")
    @PostMapping("/event/send/{id}")
    Response<Boolean> send(@PathVariable  String id);
}
