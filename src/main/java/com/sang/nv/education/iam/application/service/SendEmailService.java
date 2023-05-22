package com.sang.nv.education.iam.application.service;


import com.sang.nv.education.iam.domain.User;
import org.springframework.messaging.MessagingException;

public interface SendEmailService {

    void send(User user, String templateName, String titleKey, String token)
            throws MessagingException, javax.mail.MessagingException;

}
