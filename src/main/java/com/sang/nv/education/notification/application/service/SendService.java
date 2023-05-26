package com.sang.nv.education.notification.application.service;


import com.sang.nv.education.notification.domain.Event;

public interface SendService {

    void sendEmail(Event event);

    void sendNotification(Event event);

    void sendNotification(String eventId);

}
