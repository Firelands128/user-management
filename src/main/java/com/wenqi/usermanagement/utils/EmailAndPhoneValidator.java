/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.utils;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.context.annotation.Bean;

public class EmailAndPhoneValidator {
    @Bean
    public static boolean emailValidate(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    @Bean
    public static boolean phoneValidate(String phone) {
        String pattern = "^[1][0-9]{10}$";
        return phone.matches(pattern);
    }
}
