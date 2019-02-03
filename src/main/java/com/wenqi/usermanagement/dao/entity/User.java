/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.entity;

import com.wenqi.usermanagement.constants.UserGender;
import com.wenqi.usermanagement.constants.UserStatus;
import com.wenqi.usermanagement.constants.UserType;
import com.wenqi.usermanagement.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements UserDetails {

//    private boolean isAccountNonExpired = true;
//    private boolean isAccountNonLocked = true;
//    private boolean isCredentialsNonExpired = true;
//    private boolean isEnabled = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "status")
    private UserStatus status;

    @Column(name = "user_type")
    private UserType userType;

    @Column(name = "phone", unique = true)
    private String phone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "avatarUrl")
    private String avatarUrl;

    @Column(name = "gender")
    private UserGender gender;

    @Column(name = "birthday")
    private Date birthday;

    @Column(name = "area")
    private String area;

    @Column(name = "address")
    private String address;

    @Column(name = "county")
    private String county;

    @Column(name = "city")
    private String city;

    @Column(name = "province")
    private String province;

    @Column(name = "country")
    private String country;

    @Column(name = "others")
    private String others;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ResetPasswordToken> resetPasswordTokens = new HashSet<>();

    public User() {

    }

    public User(UserDTO userDTO) {
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
        Set<UserRole> userRoles = new HashSet<>();
        userDTO.roles.forEach(roleEnum -> userRoles.add(new UserRole(this, roleEnum)));
        this.setUserRoles(userRoles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public UserGender getGender() {
        return gender;
    }

    public void setGender(UserGender gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Set<ResetPasswordToken> getResetPasswordTokens() {
        return resetPasswordTokens;
    }

    public void setResetPasswordTokens(Set<ResetPasswordToken> resetPasswordTokens) {
        this.resetPasswordTokens = resetPasswordTokens;
    }
    //    public void setAccountNonExpired(boolean accountNonExpired) {
//        isAccountNonExpired = accountNonExpired;
//    }
//
//    public void setAccountNonLocked(boolean accountNonLocked) {
//        isAccountNonLocked = accountNonLocked;
//    }
//
//    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
//        isCredentialsNonExpired = credentialsNonExpired;
//    }
//
//    public void setEnabled(boolean enabled) {
//        isEnabled = enabled;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach(ur -> authorities.add(new SimpleGrantedAuthority(ur.getRole().toString())));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
