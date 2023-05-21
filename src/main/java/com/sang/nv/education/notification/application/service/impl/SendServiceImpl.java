package com.sang.nv.education.notification.application.service.impl;

import com.sang.nv.education.notification.application.service.SendService;
import com.sang.nv.education.notification.domain.Event;
import com.sang.nv.education.notification.domain.repository.EventDomainRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendServiceImpl implements SendService {
    private final EventDomainRepository eventDomainRepository;
    @Override
    public void sendEmail(Event event) {

    }

    @Override
    public void sendNotification(Event event) {

    }


    public void sendNotification(String eventId) {
        Event event = eventDomainRepository.getById(eventId);
        // send notification
        event.sendNotification();
        this.eventDomainRepository.save(event);
    }
}
