package com.sang.nv.education.iam.application.service.impl;

import com.sang.common.email.MailService;
import com.sang.nv.education.iam.application.service.SendEmailService;
import com.sang.nv.education.iam.domain.User;
import com.sang.nv.education.iam.infrastructure.support.enums.UserType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.util.Locale;

@Service
@Slf4j
public class SendEmailServiceImpl implements SendEmailService {

    private static final String USER = "user";
    private static final String TOKEN = "token";
    private static final String LINK = "redirectLink";
    @Value("${app.iam.domain}")
    private String domain;
    @Value("${app.iam.domain_client}")
    private String domainClient;
    private final MailService mailService;
    private final SpringTemplateEngine templateEngine;
    private final String redirectLink = "authentication/reset-password";

    public SendEmailServiceImpl(
            MailService mailService,
            SpringTemplateEngine templateEngine) {
        this.mailService = mailService;
        this.templateEngine = templateEngine;
    }

    @Override
    @Async
    public void send(User user, String templateName, String titleKey, String token) throws MessagingException {
        if (user.getEmail() == null) {
            log.debug("Email doesn't exist for user '{}'", user.getUsername());
            return;
        }
        Locale locale = Locale.forLanguageTag("vi");
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(TOKEN, token);
        context.setVariable(LINK, String.format("%s/#/%s", user.getUserType().equals(UserType.MANAGER)? domain : domainClient, redirectLink));
        String content = templateEngine.process(templateName, context);
        mailService.sendHtmlMail(user.getEmail(), titleKey, content);
    }
}
