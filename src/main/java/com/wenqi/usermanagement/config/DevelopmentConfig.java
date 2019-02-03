/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.config;

import com.wenqi.usermanagement.service.EmailService;
import com.wenqi.usermanagement.service.PhoneService;
import com.wenqi.usermanagement.service.impl.MockEmailService;
import com.wenqi.usermanagement.service.impl.MockPhoneService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevelopmentConfig {
    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }

    @Bean
    public PhoneService phoneService() {
        return new MockPhoneService();
    }
}
