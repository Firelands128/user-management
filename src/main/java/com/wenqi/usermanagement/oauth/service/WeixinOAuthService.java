/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.oauth.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.constants.UserGender;
import com.wenqi.usermanagement.dto.OAuthUserDTO;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import com.wenqi.usermanagement.oauth.api.WeixinApi;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.*;
import org.scribe.oauth.OAuth20ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
public class WeixinOAuthService extends OAuth20ServiceImpl implements CustomOAuthService{
	// The application logger
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final WeixinApi api;
	private final OAuthConfig config;
	private final String authorizationUrl;

	public WeixinOAuthService(DefaultApi20 api, OAuthConfig config) {
		super(api, config);
		this.api = (WeixinApi) api;
		this.config = config;
		this.authorizationUrl = getAuthorizationUrl(null);
	}

	@Override
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
		request.addQuerystringParameter("appid", config.getApiKey());
		request.addQuerystringParameter("secret", config.getApiSecret());
		request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
		if(config.hasScope())  request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
		Response response = request.send();
		String responseBody = response.getBody();
		Object result = JSON.parse(responseBody);
        String accessToken;
        try {
            accessToken = JSONPath.eval(result, ".access_token").toString();
        } catch (NullPointerException e) {
            String errmsg = JSONPath.eval(result, "$.errmsg").toString();
            logger.warn("OAuth code has been used." + errmsg);
            throw new MyException(ErrorCode.BAD_REQUEST, "OAuth code has been used." + errmsg);
        }
        return new Token(accessToken, "", responseBody);
	}

	@Override
	public OAuthType getOAuthType() {
		return OAuthType.WEIXIN;
	}

	@Override
	public String getAuthorizationUrl() {
		return authorizationUrl;
	}

	@Override
    public OAuthUserDTO getOAuthUserInfo(String accessToken, String openId) {
		if (accessToken == null || accessToken.length() == 0) {
			logger.warn("AccessToken {} invalid.", accessToken);
			throw new MyException(ErrorCode.BAD_REQUEST, "AccessToken " + accessToken + " invalid.");
		}
        if (openId == null || openId.length() == 0) {
            logger.warn("OpenId {} invalid.", openId);
            throw new MyException(ErrorCode.BAD_REQUEST, "OpenId " + openId + " invalid.");
        }
		OAuthUserDTO oAuthUser = new OAuthUserDTO();
		oAuthUser.oAuthType = getOAuthType();
        JSONObject userInfo = getUserInfo(accessToken, openId);
		oAuthUser.oAuthId = JSONPath.eval(userInfo, "$.openid").toString();
		oAuthUser.unionId = JSONPath.eval(userInfo, "$.unionid").toString();
        oAuthUser.name = JSONPath.eval(userInfo, "$.nickname").toString();
        String sex = JSONPath.eval(userInfo, "$.sex").toString();
        if (sex.equals("1")) {
            oAuthUser.gender = UserGender.MALE;
        } else if (sex.equals("2")) {
            oAuthUser.gender = UserGender.FEMALE;
        } else {
            oAuthUser.gender = UserGender.UNSPECIFIED;
        }
        oAuthUser.avatarUrl = JSONPath.eval(userInfo, "$.headimgurl").toString();
        oAuthUser.city = JSONPath.eval(userInfo, "$.city").toString();
        oAuthUser.province = JSONPath.eval(userInfo, "$.province").toString();
        oAuthUser.country = JSONPath.eval(userInfo, "$.country").toString();
		return oAuthUser;
	}

    public JSONObject getUserInfo(String accessToken, String openId) {
		OAuthRequest request = new OAuthRequest(api.getAccessTokenVerb(), api.getUserInfoEndPoint());
        request.addQuerystringParameter("access_token", accessToken);
		request.addQuerystringParameter("openid", openId);
		Response response = request.send();
		return JSON.parseObject(response.getBody());
	}
}
