package com.sang.nv.education.notification.web.impl;


import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.notification.application.dto.request.NotificationMarkReadRequest;
import com.sang.nv.education.notification.application.dto.request.NotificationSearchRequest;
import com.sang.nv.education.notification.application.service.NotificationService;
import com.sang.nv.education.notification.domain.Notification;
import com.sang.nv.education.notification.web.NotificationResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class NotificationResourceImpl implements NotificationResource {

    private final NotificationService notificationService;

    @Override
    public PagingResponse<Notification> findAllByUserLogin(NotificationSearchRequest params) {
        return PagingResponse.of(this.notificationService.findAllByUserLogin(params));
    }

    @Override
    public Response<Notification> findByEventId(String eventId) {
        Notification notification = notificationService.getNotificationByEventId(eventId);
        return Response.of(notification);
    }

    @Override
    public Response<Notification> findById(String id) {
        Notification notification = notificationService.getNotificationById(id);
        return Response.of(notification);
    }

    @Override
    public Response<Boolean> markReadByIds(NotificationMarkReadRequest notificationMarkReadRequest) {
        Boolean response = notificationService.markAllRead(notificationMarkReadRequest);
        return Response.of(response);
    }

    @Override
    public Response<Boolean> markReadAll() {
        Boolean res = notificationService.markReadAll();
        return Response.of(res);
    }

    @Override
    public Response<Long> countUnreadNotification() {
        return Response.of(notificationService.countUnreadNotification());
    }
}
