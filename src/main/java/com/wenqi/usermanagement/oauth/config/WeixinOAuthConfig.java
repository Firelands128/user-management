/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.oauth.config;

import com.wenqi.usermanagement.oauth.api.WeixinApi;
import com.wenqi.usermanagement.oauth.service.CustomOAuthService;
import org.scribe.builder.ServiceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeixinOAuthConfig {

    private static final String host = "https://www.host.com/";
    private static final String CALLBACK_URL = host + "oauth/%s/callback";

    @Value("${oAuth.weixin.appId}")
    private String weixinAppId;

    @Value("${oAuth.weixin.appSecret}")
    private String weixinAppSecret;

    @Bean
    public CustomOAuthService getWeixinOAuthService() {
        return (CustomOAuthService) new ServiceBuilder()
                .provider(WeixinApi.class)
                .apiKey(weixinAppId)
                .apiSecret(weixinAppSecret)
                .scope("snsapi_login")
                .callback(String.format(CALLBACK_URL, OAuthTypes.WEIXIN))
                .build();

    }
}
