/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service;

import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.constants.UserStatus;
import com.wenqi.usermanagement.dto.OAuthUserDTO;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.Optional;

public interface OAuthUserService {
    OAuthUserDTO addUser(OAuthUserDTO oAuthUserDTO, int depth);

    OAuthUserDTO getOAuthUserByOpenId(OAuthType type, String accessToken, String openId);

    OAuth2AccessToken getAccessToken(OAuthType type, UserStatus status, String username, String password);

    Optional<OAuthUserDTO> getOAuthUserByUsername(String username);
}
