/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.config;

import com.wenqi.usermanagement.service.EmailService;
import com.wenqi.usermanagement.service.PhoneService;
import com.wenqi.usermanagement.service.impl.AliyunPhoneService;
import com.wenqi.usermanagement.service.impl.MockEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProductionConfig {
    @Bean
    public EmailService emailService() {
        return new MockEmailService();
    }

    @Bean
    public PhoneService phoneService() {
        return new AliyunPhoneService();
    }
}
