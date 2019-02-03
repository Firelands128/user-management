/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    void sendResetPasswordEmail(String token, String email);

    void sendGenericEmail(SimpleMailMessage simpleMailMessage);
}
