/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "reset_password_token")
public class ResetPasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "token", unique = true)
    private String token;

    @Column(name = "expire_timestamp")
    private Timestamp expireTime;

    public ResetPasswordToken() {

    }

    public ResetPasswordToken(User user, String token, Timestamp expireTime) {
        this.user = user;
        this.token = token;
        this.expireTime = expireTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResetPasswordToken that = (ResetPasswordToken) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "ResetPasswordToken{" +
                "id=" + id +
                ", user=" + user +
                ", token='" + token + '\'' +
                ", expireTime=" + expireTime +
                '}';
    }
}
