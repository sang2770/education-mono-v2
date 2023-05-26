package com.sang.nv.education.notification.web;

import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.commonmodel.validator.anotations.ValidatePaging;
import com.sang.nv.education.notification.application.dto.request.NotificationMarkReadRequest;
import com.sang.nv.education.notification.application.dto.request.NotificationSearchRequest;
import com.sang.nv.education.notification.domain.Notification;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Api(tags = "Notification resource")
@RequestMapping("/api")
public interface NotificationResource {
    @ApiOperation(value = "Find notification by user login with paging")
    @GetMapping("/me/notifications")
    PagingResponse<Notification> findAllByUserLogin(@ValidatePaging NotificationSearchRequest params);

    @ApiOperation(value = "Find notification by event id")
    @GetMapping("/me/notifications/read-by-event-id/{eventId}")
    Response<Notification> findByEventId(@PathVariable String eventId);

    @ApiOperation(value = "Find notification by id")
    @GetMapping("/me/notifications/{id}")
    Response<Notification> findById(@PathVariable String id);

    @ApiOperation(value = "Mark as read notification by ids")
    @PostMapping("/me/notifications/mark-read-by-ids")
    Response<Boolean> markReadByIds(@RequestBody @Valid NotificationMarkReadRequest notificationMarkReadRequest);

    @ApiOperation(value = "Mark as read notification by id")
    @PostMapping("/me/notifications/{id}/mark-read")
    Response<Boolean> markReadById(@PathVariable String id);



    @ApiOperation(value = "Mark as read all notification")
    @PostMapping("/me/notifications/mark-read-all")
    Response<Boolean> markReadAll();

    @ApiOperation(value = "Count unread notification")
    @GetMapping("/me/notifications/count-unread")
    Response<Long> countUnreadNotification();
}
