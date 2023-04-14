package com.sang.nv.education.notification.rest.impl;


import com.sang.commonmodel.dto.response.PagingResponse;
import com.sang.commonmodel.dto.response.Response;
import com.sang.nv.education.notification.application.dto.request.NotificationDeleteRequest;
import com.sang.nv.education.notification.application.dto.request.NotificationMarkReadRequest;
import com.sang.nv.education.notification.application.dto.request.NotificationMarkUnreadRequest;
import com.sang.nv.education.notification.application.dto.request.NotificationSearchRequest;
import com.sang.nv.education.notification.application.service.NotificationService;
import com.sang.nv.education.notification.domain.Notification;
import com.sang.nv.education.notification.rest.NotificationResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class NotificationResourceImpl implements NotificationResource {

    private final NotificationService notificationService;

    public NotificationResourceImpl(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Override
    public PagingResponse<Notification> findAllByUserLogin(NotificationSearchRequest params) {
        return PagingResponse.of(notificationService.findAllByUserLogin(params));
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
    public Response<Boolean> markReadByIds(
            NotificationMarkReadRequest notificationMarkReadRequest) {
        Boolean response = notificationService.markAllRead(notificationMarkReadRequest);
        return Response.of(response);
    }

    @Override
    public Response<Boolean> markUnReadByIds(NotificationMarkUnreadRequest request) {
        Boolean response = notificationService.markAllUnreadByIds(request);
        return Response.of(response);
    }

    @Override
    public Response<Boolean> markRead(String id) {
        Boolean res = notificationService.markRead(id);
        return Response.of(res);
    }

    @Override
    public Response<Boolean> markReadAll() {
        Boolean res = notificationService.markReadAll();
        return Response.of(res);
    }

    @Override
    public Response<Boolean> delete(String id) {
        Boolean res = notificationService.delete(id);
        return Response.of(res);
    }

    @Override
    public Response<Boolean> deleteAll(NotificationDeleteRequest notificationDeleteRequest) {
        Boolean res = notificationService.deleteAll(notificationDeleteRequest);
        return Response.of(res);
    }

    @Override
    public Response<Long> countUnreadNotification() {
        return Response.of(notificationService.countUnreadNotification());
    }
}
