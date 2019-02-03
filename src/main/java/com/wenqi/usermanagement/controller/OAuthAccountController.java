/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONPath;
import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.dto.OAuthUserDTO;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import com.wenqi.usermanagement.oauth.service.CustomOAuthService;
import com.wenqi.usermanagement.oauth.service.OAuthServices;
import com.wenqi.usermanagement.service.impl.OAuthUserServiceImpl;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(value = "/oauth", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OAuthAccountController {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());


    private final OAuthServices oAuthServices;
    private final OAuthUserServiceImpl oAuthUserService;

    @Autowired
    public OAuthAccountController(OAuthServices oAuthServices,
                                  OAuthUserServiceImpl oAuthUserService) {
        this.oAuthServices = oAuthServices;
        this.oAuthUserService = oAuthUserService;
    }

    /**
     * Get oAuth login redirect URL
     * <p>
     *     URL path: "/oauth/{type}/login"
     * </p>
     * <p>
     *     HTTP method: GET
     * </p>
     * @param type The oAuth type
     * @return OAuth login redirect URL
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{type}/login")
    public ResponseEntity<String> showLogin(@PathVariable(name = "type") OAuthType type) {
        CustomOAuthService oAuthService = oAuthServices.getOAuthService(type);
        String redirectURL = oAuthService.getAuthorizationUrl();
        return new ResponseEntity<>(redirectURL, HttpStatus.OK);
    }

    /**
     * Get oAuth login unified token
     * <p>
     *     URL path: "/oauth/{type}/token"
     * </p>
     * <p>
     *     HTTP method: GET
     * </p>
     * @param type The oAuth type
     * @param code The authorization code
     * @param state The custom state
     * @return oAuth login unified token
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{type}/token")
    public ResponseEntity<OAuth2AccessToken> getAccessToken(@PathVariable(name = "type") OAuthType type,
                                                            @RequestParam(name = "code") String code,
                                                            @RequestParam(name = "state") String state) {
        if (!state.equals("esfadsgsad34fwdef")) {
            logger.warn("Weixin oauth API state code incorrect");
            throw new MyException(ErrorCode.BAD_REQUEST, "Weixin oauth API state code incorrect");
        }
        CustomOAuthService oAuthService = oAuthServices.getOAuthService(type);
        Token token = oAuthService.getAccessToken(null, new Verifier(code));
        if (null == token) {
            logger.error("Get access token by code {} failed.", code);
            throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        String accessToken = token.getToken();
        String rawResponse = token.getRawResponse();
        Object json = JSON.parse(rawResponse);
        String openId = JSONPath.eval(json, "$.openid").toString();
        OAuthUserDTO oAuthUserDTO = oAuthUserService.getOAuthUserByOpenId(type, accessToken, openId);

        OAuth2AccessToken auth2AccessToken = oAuthUserService.getAccessToken(type, oAuthUserDTO.status,
                oAuthUserDTO.username, oAuthUserDTO.password);
        return new ResponseEntity<>(auth2AccessToken, HttpStatus.OK);
    }

    /**
     * Get oAuth user info
     * <p>
     *     URL path: "/oauth/{type}/info"
     * </p>
     * <p>
     *     HTTP method: GET
     * </p>
     * @param type The oAuth type
     * @param authentication The authentication with access token
     * @return oAuth user info
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{type}/info")
    public ResponseEntity<OAuthUserDTO> getOAuthUser(@PathVariable(name = "type") OAuthType type,
                                                     Authentication authentication) {
        if (null == authentication) {
            logger.warn("There is no authentication provided when get oauth user info");
            throw new MyException(ErrorCode.FORBIDDEN, "There is no authentication provided.");
        }
        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("OAUTH_" + type.toString()))) {
            logger.warn("The authentication doesn't match this oauth type {}.", type.toString());
            throw new MyException(ErrorCode.FORBIDDEN, "The authentication doesn't match this oauth type " + type.toString() + ".");
        }
        String username = authentication.getName();
        Optional<OAuthUserDTO> oAuthUserDTO = oAuthUserService.getOAuthUserByUsername(username);
        if (!oAuthUserDTO.isPresent()) {
            throw new MyException(ErrorCode.NOT_FOUND, "The oauth user with username " + username + " not found.");
        }
        return ResponseEntity.ok(oAuthUserDTO.get());
    }
}
