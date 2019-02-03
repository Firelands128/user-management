/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.oauth.service;

import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.dto.OAuthUserDTO;
import org.scribe.oauth.OAuthService;

public interface CustomOAuthService extends OAuthService{

    OAuthType getOAuthType();

    String getAuthorizationUrl();

    OAuthUserDTO getOAuthUserInfo(String accessToken, String openId);

}
