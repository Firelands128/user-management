/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.entity;

import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.dto.OAuthUserDTO;

import javax.persistence.*;

@Entity
@Table(name = "oauth_user")
public class OAuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "oauth_type")
    private OAuthType oAuthType;

    @Column(name = "oauth_id")
    private String oAuthId;

    @Column(name = "union_id")
    private String unionId;

    public OAuthUser() {

    }

    public OAuthUser(User user,
                     OAuthType oAuthType,
                     String oAuthId,
                     String unionId) {
        this.user = user;
        this.oAuthType = oAuthType;
        this.oAuthId = oAuthId;
        this.unionId = unionId;
    }

    public OAuthUser(OAuthUserDTO oAuthUserDTO) {
        this.oAuthType = oAuthUserDTO.oAuthType;
        this.oAuthId = oAuthUserDTO.oAuthId;
        this.unionId = oAuthUserDTO.unionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OAuthType getOAuthType() {
        return oAuthType;
    }

    public void setOAuthType(OAuthType oAuthType) {
        this.oAuthType = oAuthType;
    }

    public String getOAuthId() {
        return oAuthId;
    }

    public void setOAuthId(String oAuthId) {
        this.oAuthId = oAuthId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OAuthUser oAuthUser = (OAuthUser) o;

        return id.equals(oAuthUser.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "OAuthUser{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", oAuthType='" + oAuthType + '\'' +
                ", oAuthId='" + oAuthId + '\'' +
                ", unionId='" + unionId + '\'' +
                '}';
    }
}
