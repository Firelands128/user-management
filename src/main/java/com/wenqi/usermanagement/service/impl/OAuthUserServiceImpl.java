/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.constants.UserStatus;
import com.wenqi.usermanagement.dao.entity.OAuthUser;
import com.wenqi.usermanagement.dao.entity.User;
import com.wenqi.usermanagement.dao.repository.OAuthUserRepository;
import com.wenqi.usermanagement.dto.OAuthUserDTO;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import com.wenqi.usermanagement.oauth.service.CustomOAuthService;
import com.wenqi.usermanagement.oauth.service.OAuthServices;
import com.wenqi.usermanagement.service.OAuthUserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
public class OAuthUserServiceImpl implements OAuthUserService {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final int randomUsernameLength = 16;
    private final int randomPasswordLength = 16;
    private final GeneralUserServiceImpl userService;
    private final DefaultTokenServices tokenServices;
    private final AuthenticationManager authenticationManager;
    @Value("${security.jwt.web-client-id}")
    private String webClientId;
    @Value("${security.jwt.web-client-secret}")
    private String webClientSecret;
    @Value("${security.jwt.grant-type}")
    private String grantType;
    @Value("${security.jwt.scope-read}")
    private String scopeRead;
    private final OAuthServices oAuthServices;
    private final OAuthUserRepository oAuthUserRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${security.jwt.scope-write}")
    private String scopeWrite = "write";
    @Value("${security.jwt.resource-ids}")
    private String resourceIds;

    @Autowired
    public OAuthUserServiceImpl(GeneralUserServiceImpl userService,
                                OAuthServices oAuthServices,
                                OAuthUserRepository oAuthUserRepository,
                                PasswordEncoder passwordEncoder, DefaultTokenServices tokenServices, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.oAuthServices = oAuthServices;
        this.oAuthUserRepository = oAuthUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenServices = tokenServices;
        this.authenticationManager = authenticationManager;
    }

    private static String generateRandomString(int length) {
        String s = RandomStringUtils.randomAlphanumeric(length);
        return s;
    }

    @Override
    public OAuthUserDTO addUser(OAuthUserDTO oAuthUserDTO, int depth) {
        User user = new User(oAuthUserDTO);
        user.setUsername(oAuthUserDTO.oAuthType.toString() + "_" + generateRandomString(randomUsernameLength));
        user.setPassword(passwordEncoder.encode(generateRandomString(randomPasswordLength)));
        user.setStatus(UserStatus.UNREGISTERED);

        // set recursive depth limit is 3
        if (userService.checkUsernameValid(user.getUsername()).isPresent() && depth < 3) {
            return addUser(oAuthUserDTO, depth + 1);
        }
        if (depth >= 3) {
            logger.error("Fail to generate random username from oauth user. Tried 3 times.");
            throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        user = userService.addUser(new User(oAuthUserDTO), true);
        OAuthUser newOAuthUser = new OAuthUser(oAuthUserDTO);
        newOAuthUser.setUser(user);
        newOAuthUser = oAuthUserRepository.save(newOAuthUser);
        return new OAuthUserDTO(newOAuthUser);
    }

    @Override
    public OAuthUserDTO getOAuthUserByOpenId(OAuthType type, String accessToken, String openId) {
        Optional<OAuthUserDTO> oAuthUserDTO = this.getOAuthUserByOAuthTypeAndOAuthId(type, openId);
        if (!oAuthUserDTO.isPresent()) {
            CustomOAuthService oAuthService = oAuthServices.getOAuthService(type);
            OAuthUserDTO oAuthUserInfo = oAuthService.getOAuthUserInfo(accessToken, openId);
            return addUser(oAuthUserInfo, 0);
        } else {
            return oAuthUserDTO.get();
        }
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuthType type, UserStatus status, String username, String password) {
        Authentication auth;
        if (status.equals(UserStatus.UNREGISTERED)) {
            auth = new UsernamePasswordAuthenticationToken(username, password,
                    Collections.singletonList(new SimpleGrantedAuthority("OAUTH_" + type.toString())));
        } else {
            UsernamePasswordAuthenticationToken authreq =
                    new UsernamePasswordAuthenticationToken(username, password);
            auth = authenticationManager.authenticate(authreq);
        }
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(
                new OAuth2Request(null, null, null, true, null,
                        null, null, null, null), auth);
        OAuth2AccessToken oAuth2AccessToken = tokenServices.createAccessToken(oAuth2Authentication);
        return oAuth2AccessToken;
    }

    @Override
    public Optional<OAuthUserDTO> getOAuthUserByUsername(String username) {
        User user = userService.getUserByUsername(username);
        Optional<OAuthUser> oAuthUser = oAuthUserRepository.findByUser(user);
        return oAuthUser.map(OAuthUserDTO::new);
    }


    private Optional<OAuthUserDTO> getOAuthUserByOAuthTypeAndOAuthId(OAuthType oAuthType, String oAuthId) {
        Optional<OAuthUser> oAuthUser = oAuthUserRepository.findByOAuthTypeAndOAuthId(oAuthType, oAuthId);
        return oAuthUser.map(OAuthUserDTO::new);
    }
}
