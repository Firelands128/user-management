/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.dao.entity.OrganizationProfile;
import com.wenqi.usermanagement.dao.entity.User;
import com.wenqi.usermanagement.dao.repository.OrganizationProfileRepository;
import com.wenqi.usermanagement.dto.OrganizationUserDTO;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class OrganizationUserService {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrganizationProfileRepository organizationProfileRepository;

    @Autowired
    public OrganizationUserService(OrganizationProfileRepository organizationProfileRepository) {
        this.organizationProfileRepository = organizationProfileRepository;
    }

    protected OrganizationUserDTO addUser(User user, OrganizationUserDTO organizationUserDTO, boolean oauth) {
        checkParameterValid(organizationUserDTO, false);
        OrganizationProfile organizationProfile = new OrganizationProfile(organizationUserDTO);
        organizationProfile.setUser(user);
        organizationProfile = organizationProfileRepository.save(organizationProfile);
        logger.info("Organization User with username {} created.", organizationProfile.getUser().getUsername());
        return new OrganizationUserDTO(organizationProfile);
    }

    protected Optional<OrganizationUserDTO> getByUserId(long userId) {
        Optional<OrganizationProfile> organizationProfile = organizationProfileRepository.findByUserId(userId);
        return organizationProfile.map(OrganizationUserDTO::new);
    }

    protected OrganizationUserDTO updateUser(User updatedUser, OrganizationUserDTO organizationUserDTO) {
        checkParameterValid(organizationUserDTO, true);

        Optional<OrganizationProfile> existedOrganizationProfileOp = organizationProfileRepository.findByUserId(updatedUser.getId());
        if (!existedOrganizationProfileOp.isPresent()) {
            logger.error("User with username {} isn't organization user.", updatedUser.getUsername());
            throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        OrganizationProfile existedOrganizationProfile = existedOrganizationProfileOp.get();

        OrganizationProfile updatedOrganizationProfile = updateOrganizationProfile(existedOrganizationProfile, organizationUserDTO);
        updatedOrganizationProfile.setUser(updatedUser);

        organizationProfileRepository.save(updatedOrganizationProfile);
        logger.info("Organization user with username {} updated.", updatedUser.getUsername());
        return new OrganizationUserDTO(updatedOrganizationProfile);
    }

    private OrganizationProfile updateOrganizationProfile(OrganizationProfile organizationProfile, OrganizationUserDTO organizationUserDTO) {
        if (organizationUserDTO.idParent != null) {
            organizationProfile.setIdParent(organizationUserDTO.idParent);
        }
        if (organizationUserDTO.officerName != null) {
            organizationProfile.setOfficerName(organizationUserDTO.officerName);
        }
        if (organizationUserDTO.officerPhone != null) {
            organizationProfile.setOfficerPhone(organizationUserDTO.officerPhone);
        }
        if (organizationUserDTO.description != null) {
            organizationProfile.setDescription(organizationUserDTO.description);
        }
        if (organizationUserDTO.license != null) {
            organizationProfile.setLicense(organizationUserDTO.license);
        }
        if (organizationUserDTO.licenseCopy != null) {
            organizationProfile.setLicenseCopy(organizationUserDTO.licenseCopy);
        }
        if (organizationUserDTO.type != null) {
            organizationProfile.setType(organizationUserDTO.type);
        }
        return organizationProfile;
    }

    private void checkParameterValid(OrganizationUserDTO organizationUserDTO, boolean update) {
        int officer_name_length = 32;
        int officer_phone_length = 16;
        int description_length = 200;
        int license_length = 100;
        int license_copy_length = 100;

        if (organizationUserDTO.idParent != null) {
            if (!organizationProfileRepository.findById(organizationUserDTO.idParent).isPresent()) {
                logger.warn("Parent organization {} doesn't exists.", organizationUserDTO.idParent);
                throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                        "Parent organization " + organizationUserDTO.idParent + " doesn't exists.");
            }
        }
        if (organizationUserDTO.officerName == null && !update) {
            logger.warn("Officer name cannot be null.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Officer name cannot be null.");
        }
        if (organizationUserDTO.officerName != null && organizationUserDTO.officerName.length() > officer_name_length) {
            logger.warn("The length of officer name cannot be greater than {}.", officer_name_length);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of officer name cannot be greater than " + officer_name_length + ".");
        }
        if (organizationUserDTO.officerPhone == null && !update) {
            logger.warn("Officer phone cannot be null.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Officer phone cannot be null.");
        }
        if (organizationUserDTO.officerPhone != null && organizationUserDTO.officerPhone.length() > officer_name_length) {
            logger.warn("The length of officer phone cannot be greater than {}.", officer_phone_length);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of officer phone cannot be greater than " + officer_phone_length + ".");
        }
        if (organizationUserDTO.description != null && organizationUserDTO.description.length() > description_length) {
            logger.warn("The length of description cannot be greater than {}.", description_length);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of description cannot be greater than " + description_length + ".");
        }
        if (organizationUserDTO.license == null && !update) {
            logger.warn("License cannot be null.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "License cannot be null.");
        }
        if (organizationUserDTO.license != null) {
            if (organizationUserDTO.license.length() > license_length) {
                logger.warn("The length of license cannot be greater than {}.", license_length);
                throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                        "The length of license cannot be greater than " + license_length + ".");
            }
            if (organizationProfileRepository.findByLicense(organizationUserDTO.license).isPresent()) {
                logger.warn("License {} have already exists.", organizationUserDTO.license);
                throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                        "License " + organizationUserDTO.license + " have already exists.");
            }
        }
        if (organizationUserDTO.licenseCopy != null && organizationUserDTO.licenseCopy.length() > license_copy_length) {
            logger.warn("The length of license copy cannot be greater than {}.", license_copy_length);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of license copy cannot be greater than " + license_copy_length + ".");
        }
    }
}
