/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.security.config;

import com.wenqi.usermanagement.security.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private final AuthenticationManager authenticationManager;
    private final UserSecurityService userSecurityService;
    private final DefaultTokenServices defaultTokenServices;
    private final AccessTokenConverter accessTokenConverter;
    private final PasswordEncoder passwordEncoder;
    @Value("${security.jwt.web-client-id}")
    private String webClientId;
    @Value("${security.jwt.web-client-secret}")
    private String webClientSecret;
    @Value("${security.jwt.grant-type}")
    private String grantType;
    @Value("${security.jwt.scope-read}")
    private String scopeRead;
    @Value("${security.jwt.scope-write}")
    private String scopeWrite = "write";
    @Value("${security.jwt.resource-ids}")
    private String resourceIds;
    @Value("${security.token-url}")
    private String tokenUrl;

    @Autowired
    public AuthorizationServerConfig(AuthenticationManager authenticationManager,
                                     UserSecurityService userSecurityService,
                                     DefaultTokenServices defaultTokenServices,
                                     AccessTokenConverter accessTokenConverter,
                                     ClientDetailsService clientDetailsService,
                                     PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userSecurityService = userSecurityService;
        this.defaultTokenServices = defaultTokenServices;
        this.accessTokenConverter = accessTokenConverter;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer//TODO client database config
                .inMemory()
                .withClient(webClientId)
                .secret(passwordEncoder.encode(webClientSecret))
                .authorizedGrantTypes(grantType)
                .scopes(scopeRead, scopeWrite)
                .resourceIds(resourceIds);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenServices(defaultTokenServices)
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter)
                .userDetailsService(userSecurityService)
                .pathMapping("/oauth/token", tokenUrl);
    }
}
