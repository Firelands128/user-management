/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.reset.email.from}")
    private String defaultEmailFrom;

    @Value("${reset.password.subject}")
    private String messageSubject;

    @Value("${reset.password.text1}")
    private String text1;
    @Value("${reset.password.text2}")
    private String text2;
    @Value("${reset.password.text3}")
    private String text3;

    protected SimpleMailMessage prepareSimpleMailMessage(String token, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(defaultEmailFrom);
        message.setSubject(messageSubject);
        message.setText(text1 + token + text2 + text3);
        return message;
    }

    @Override
    public void sendResetPasswordEmail(String token, String email) {
        sendGenericEmail(prepareSimpleMailMessage(token, email));
    }
}
