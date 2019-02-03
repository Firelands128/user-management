/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.oauth.service;

import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OAuthServices {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final List<CustomOAuthService> oAuthServiceDecorators;

    @Autowired
    public OAuthServices(List<CustomOAuthService> oAuthServiceDecorators) {
        this.oAuthServiceDecorators = oAuthServiceDecorators;
    }

    public CustomOAuthService getOAuthService(OAuthType type) {
        Optional<CustomOAuthService> oAuthService = oAuthServiceDecorators.stream().filter(o -> o.getOAuthType().equals(type)).findFirst();
        if (!oAuthService.isPresent()) {
            logger.warn("Invalid Oauth type {}.", type);
            throw new MyException(ErrorCode.BAD_REQUEST, "Invalid Oauth type " + type + ".");
        }
        return oAuthService.get();
    }

    public List<CustomOAuthService> getAllOAuthServices(){
        return oAuthServiceDecorators;
    }
}
