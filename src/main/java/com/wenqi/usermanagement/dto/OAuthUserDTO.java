/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.dao.entity.OAuthUser;
import com.wenqi.usermanagement.dao.entity.User;

import java.util.HashSet;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class OAuthUserDTO extends UserDTO {
    public Long oAuthUserId;
    public OAuthType oAuthType;
    public String oAuthId;
    public String unionId;

    public OAuthUserDTO() {

    }

    public OAuthUserDTO(OAuthUser oAuthUser) {
        this.oAuthUserId = oAuthUser.getId();
        this.oAuthType = oAuthUser.getOAuthType();
        this.oAuthId = oAuthUser.getOAuthId();
        this.unionId = oAuthUser.getUnionId();

        User user = oAuthUser.getUser();
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.userType = user.getUserType();
        if (user.getUserRoles() != null && user.getUserRoles().size() > 0) {
            this.roles = new HashSet<>(user.getUserRoles().size());
            user.getUserRoles().forEach(userRole -> roles.add(userRole.getRole()));
        }
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.name = user.getName();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
        this.avatarUrl = user.getAvatarUrl();
        this.area = user.getArea();
        this.address = user.getAddress();
        this.county = user.getCounty();
        this.city = user.getCity();
        this.province = user.getProvince();
        this.country = user.getCountry();
        this.others = user.getOthers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OAuthUserDTO that = (OAuthUserDTO) o;

        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "OAuthUserDTO{" +
                "oAuthUserId=" + oAuthUserId +
                ", oAuthType=" + oAuthType +
                ", oAuthId='" + oAuthId + '\'' +
                ", unionId='" + unionId + '\'' +
                '}';
    }
}
