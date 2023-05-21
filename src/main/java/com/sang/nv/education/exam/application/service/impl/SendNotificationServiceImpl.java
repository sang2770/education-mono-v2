package com.sang.nv.education.exam.application.service.impl;

import com.sang.nv.education.exam.application.service.SendNotificationService;
import com.sang.nv.education.exam.infrastructure.support.Constant;
import com.sang.nv.education.notification.application.dto.request.IssueEventRequest;
import com.sang.nv.education.notification.application.service.EventService;
import com.sang.nv.education.notification.application.service.NotificationTemplateService;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.domain.NotificationTemplate;
import com.sang.nv.education.notification.infrastructure.support.enums.EventType;
import com.sang.nv.education.notification.infrastructure.support.enums.NotificationCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SendNotificationServiceImpl implements SendNotificationService {
    @Value("${app.iam.domain}")
    private String domain;

    @Value("${app.iam.domain_client}")
    private String domainClient;

    private final NotificationTemplateService notificationTemplateService;

    private final EventService eventService;
    public SendNotificationServiceImpl(NotificationTemplateService notificationTemplateService, EventService eventService) {
        this.notificationTemplateService = notificationTemplateService;
        this.eventService = eventService;
    }

    @Override
    public void sendNotification(NotificationCode notificationCode, List<String> targetIds, Map<String, String> extraData) {
        NotificationTemplate notificationTemplate = this.notificationTemplateService.getByCode(notificationCode);
        String title = "";
        String content = "";
        String attackedLink = "";
        switch (notificationCode){
            case ROOM_IN:
                title = notificationTemplate.getTitle();
                content = String.format(notificationTemplate.getContent(), extraData.get(Constant.ROOM_NAME), extraData.get(Constant.FULL_NAME));
                attackedLink = String.format(notificationTemplate.getUrl(), extraData.get(Constant.ROOM_ID));
                break;
            case NEW_EXAM:
                title = notificationTemplate.getTitle();
                content = String.format(notificationTemplate.getContent(), extraData.get(Constant.PERIOD_NAME), extraData.get(Constant.ROOM_NAME));
                attackedLink = String.format(notificationTemplate.getUrl(), extraData.get(Constant.ROOM_ID), extraData.get(Constant.PERIOD_ID));
                break;
            default:
                break;
        }
        IssueEventRequest request = IssueEventRequest.builder()
                .title(title)
                .content(content)
                .attachedLink(attackedLink)
                .eventType(EventType.NOTIFICATION)
                .build();

        Event event = this.eventService.issued(request);
        this.eventService.send(event.getId());

    }


}
