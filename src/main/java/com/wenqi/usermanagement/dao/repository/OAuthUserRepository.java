/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.repository;

import com.wenqi.usermanagement.constants.OAuthType;
import com.wenqi.usermanagement.dao.entity.OAuthUser;
import com.wenqi.usermanagement.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuthUserRepository extends JpaRepository<OAuthUser, String> {
    Optional<OAuthUser> findByOAuthTypeAndOAuthId(OAuthType oAuthType, String oAuthId);

    Optional<OAuthUser> findByUser(User user);
}
