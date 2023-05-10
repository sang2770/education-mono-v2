package com.sang.nv.education.iam.application.config;

import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(MailProperties.class)

public class MailConfig {
    private final MailProperties mailProperties;

    public MailConfig(MailProperties mailProperties) {
        this.mailProperties = mailProperties;
    }

    @Bean
    public JavaMailSender getJavamailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.mailProperties.getHost());
        mailSender.setPort(this.mailProperties.getPort());
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setUsername(this.mailProperties.getUsername());
        mailSender.setPassword(this.mailProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

}
