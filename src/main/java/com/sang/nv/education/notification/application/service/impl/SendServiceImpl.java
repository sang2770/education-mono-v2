package com.sang.nv.education.notification.application.service.impl;

import com.sang.nv.education.notification.application.service.SendService;
import com.sang.nv.education.notification.domain.Event;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SendServiceImpl implements SendService {
    @Override
    public void sendEmail(Event event) {

    }

    @Override
    public void sendNotification(Event event) {

    }

    @Override
    public void sendNotification(String eventId) {

    }
}
