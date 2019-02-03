/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.repository;

import com.wenqi.usermanagement.dao.entity.NormalProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NormalProfileRepository extends JpaRepository<NormalProfile, Long> {
    @Query("select p from NormalProfile p inner join p.user u where p.user.id = ?1")
    Optional<NormalProfile> findByUserId(Long userId);
}
