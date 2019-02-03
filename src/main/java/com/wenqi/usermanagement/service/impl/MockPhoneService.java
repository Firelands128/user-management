/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MockPhoneService implements PhoneService {

    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sendSMS(String token, String phone) {
        logger.debug("Simulating an phone service...");
        logger.info(token);
        logger.debug("SMS sent!");
    }
}
