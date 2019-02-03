/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.dao.repository;

import com.wenqi.usermanagement.dao.entity.OrganizationProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationProfileRepository extends JpaRepository<OrganizationProfile, Long> {
    @Query("select p from OrganizationProfile p inner join p.user u where p.user.id = ?1")
    Optional<OrganizationProfile> findByUserId(long userId);

    Optional<OrganizationProfile> findByLicense(String license);
}
