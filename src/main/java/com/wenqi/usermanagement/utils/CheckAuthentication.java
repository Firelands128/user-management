/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.utils;

import com.wenqi.usermanagement.constants.RoleEnum;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class CheckAuthentication {
    // The application logger
    private static final Logger logger = LoggerFactory.getLogger(CheckAuthentication.class);

    public static void checkAuthentication(Authentication authentication, String username) {
        if (null == authentication) {
            logger.warn("There is no authentication provided with username {}", username);
            throw new MyException(ErrorCode.FORBIDDEN, "There is no authentication provided.");
        }
        if (authentication.getName().equals(username)) {
            return;
        }
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(RoleEnum.SYSTEM_ADMIN.toString()))) {
            return;
        }
        throw new MyException(ErrorCode.FORBIDDEN, "The authentication doesn't match username and doesn't have system admin role.");
    }
}
