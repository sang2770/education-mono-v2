package com.sang.nv.education.notification.application.service.impl;

import com.sang.common.email.MailService;
import com.sang.commonmodel.validator.ValidateConstraint;
import com.sang.nv.education.iam.application.service.SendEmailService;
import com.sang.nv.education.notification.application.service.SendService;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.domain.EventTarget;
import com.sang.nv.education.notification.domain.repository.EventDomainRepository;
import com.sang.nv.education.notification.infrastructure.support.enums.EventType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class SendServiceImpl implements SendService {
    private final EventDomainRepository eventDomainRepository;
    private final MailService sendEmailService;
    @Override
    public void sendEmail(Event event) {
        List<EventTarget> eventTargetList = event.getEventTargets();
        if (!CollectionUtils.isEmpty(eventTargetList))
        {
            String emailRegex = ValidateConstraint.FORMAT.EMAIL_PATTERN;
            eventTargetList.forEach(eventTarget -> {
                if (Objects.isNull(eventTarget.getEmail()) || !eventTarget.getEmail().matches(emailRegex) )
                {
                    return;
                }
                this.sendEmailService.sendSimpleMail(eventTarget.getEmail(), event.getTitle(), event.getContent());
            });
        }
    }

    @Override
    public void sendNotification(Event event) {

    }


    public void sendNotification(String eventId) {
        Event event = eventDomainRepository.getById(eventId);
        // send notification
        event.sendNotification();
        if (event.getEventType().equals(EventType.EMAIL) || event.getEventType().equals(EventType.NOTIFICATION_EMAIL))
        {
            this.sendEmail(event);
        }
        this.eventDomainRepository.save(event);
    }
}
