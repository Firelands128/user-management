/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wenqi.usermanagement.dao.entity.NormalProfile;
import com.wenqi.usermanagement.dao.entity.User;

import java.util.HashSet;

@SuppressWarnings("WeakerAccess")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NormalUserDTO extends UserDTO {
    public Long normalProfileId;
    public Long idOrganization;
    public Long idEmployee;
    public Long idParent;
    public String school;
    public Integer grade;

	public NormalUserDTO() {

	}

    public NormalUserDTO(UserDTO userDTO) {
        this.userId = userDTO.userId;
        this.username = userDTO.username;
        this.password = userDTO.password;
        this.status = userDTO.status;
        this.userType = userDTO.userType;
        this.phone = userDTO.phone;
        this.email = userDTO.email;
        this.name = userDTO.name;
        this.avatarUrl = userDTO.avatarUrl;
        this.gender = userDTO.gender;
        this.birthday = userDTO.birthday;
        this.area = userDTO.area;
        this.address = userDTO.address;
        this.county = userDTO.county;
        this.city = userDTO.city;
        this.province = userDTO.province;
        this.country = userDTO.country;
        this.others = userDTO.others;
        this.roles = userDTO.roles;
    }

    public NormalUserDTO(NormalProfile normalProfile) {
        this.normalProfileId = normalProfile.getId();
        this.idOrganization = normalProfile.getIdOrganization();
        this.idEmployee = normalProfile.getIdEmployee();
        this.idParent = normalProfile.getIdParent();
        this.school = normalProfile.getSchool();
        this.grade = normalProfile.getGrade();

        User user = normalProfile.getUser();
        this.userId = user.getId();
        this.username = user.getUsername();
        this.status = user.getStatus();
        this.userType = user.getUserType();
        this.roles = new HashSet<>(user.getUserRoles().size());
        user.getUserRoles().forEach(userRole -> roles.add(userRole.getRole()));
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

        NormalUserDTO that = (NormalUserDTO) o;

        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

    @Override
    public String toString() {
        return "NormalUserDTO{" +
                "normalProfileId=" + normalProfileId +
                ", idOrganization=" + idOrganization +
                ", idEmployee=" + idEmployee +
                ", idParent=" + idParent +
                ", school='" + school + '\'' +
                ", grade=" + grade +
                '}';
    }
}
