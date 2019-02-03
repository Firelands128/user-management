/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.repository;

import com.wenqi.usermanagement.constants.RoleEnum;
import com.wenqi.usermanagement.dao.entity.User;
import com.wenqi.usermanagement.dao.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    Optional<UserRole> findByUserAndRole(User user, RoleEnum roleEnum);

    Set<UserRole> findByUser(User user);
}
