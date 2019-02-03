/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MockEmailService extends AbstractEmailService {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sendGenericEmail(SimpleMailMessage simpleMailMessage) {
        logger.debug("Simulating an email service...");
        logger.info(simpleMailMessage.toString());
        logger.debug("Email sent!");
    }
}
