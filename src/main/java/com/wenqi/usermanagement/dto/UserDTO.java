/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.wenqi.usermanagement.constants.RoleEnum;
import com.wenqi.usermanagement.constants.UserGender;
import com.wenqi.usermanagement.constants.UserStatus;
import com.wenqi.usermanagement.constants.UserType;
import com.wenqi.usermanagement.dao.entity.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {
    public Long userId;
    public String username;
    public String password;
    public UserStatus status;
    public UserType userType;
    public String phone;
    public String email;
    public String name;
    public String avatarUrl;
    public UserGender gender;
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date birthday;
    public String area;
    public String address;
    public String county;
    public String city;
    public String province;
    public String country;
    public String others;
    public Set<RoleEnum> roles;

    public UserDTO() {

    }

    public UserDTO(String username, String phone, String email) {
        this.username = username;
        this.phone = phone;
        this.email = email;
    }

    public UserDTO(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.status = user.getStatus();
        this.userType = user.getUserType();
        this.phone = user.getPhone();
        this.email = user.getEmail();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.gender = user.getGender();
        this.birthday = user.getBirthday();
        this.area = user.getArea();
        this.address = user.getAddress();
        this.county = user.getCounty();
        this.city = user.getCity();
        this.province = user.getProvince();
        this.country = user.getCountry();
        this.others = user.getOthers();
        Set<RoleEnum> roleEnumSet = new HashSet<>(user.getUserRoles().size());
        user.getUserRoles().forEach(userRole -> roleEnumSet.add(userRole.getRole()));
        this.roles = roleEnumSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;

        return userId.equals(userDTO.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", userType=" + userType +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", gender=" + gender +
                ", birthday=" + birthday +
                ", area='" + area + '\'' +
                ", address='" + address + '\'' +
                ", county='" + county + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", others='" + others + '\'' +
                ", roles=" + roles +
                '}';
    }
}
