/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.dao.entity.NormalProfile;
import com.wenqi.usermanagement.dao.entity.User;
import com.wenqi.usermanagement.dao.repository.EmployeeProfileRepository;
import com.wenqi.usermanagement.dao.repository.NormalProfileRepository;
import com.wenqi.usermanagement.dao.repository.OrganizationProfileRepository;
import com.wenqi.usermanagement.dto.NormalUserDTO;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class NormalUserService {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final NormalProfileRepository normalProfileRepository;
    private final OrganizationProfileRepository organizationProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    public NormalUserService(NormalProfileRepository normalProfileRepository,
                             OrganizationProfileRepository organizationProfileRepository,
                             EmployeeProfileRepository employeeProfileRepository) {
        this.normalProfileRepository = normalProfileRepository;
        this.organizationProfileRepository = organizationProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    protected NormalUserDTO addUser(User user, NormalUserDTO normalUserDTO, boolean oauth) {
        checkParameterValid(normalUserDTO, false);
        NormalProfile normalProfile = new NormalProfile(normalUserDTO);
        normalProfile.setUser(user);
        normalProfile = normalProfileRepository.save(normalProfile);
        logger.info("Normal User with username {} created.", normalProfile.getUser().getUsername());
        return new NormalUserDTO(normalProfile);
    }

    protected Optional<NormalUserDTO> getByUserId(long userId) {
        Optional<NormalProfile> normalProfile = normalProfileRepository.findByUserId(userId);
        return normalProfile.map(NormalUserDTO::new);
    }

    protected NormalUserDTO updateUser(User updatedUser, NormalUserDTO newNormalUserDTO) {
        checkParameterValid(newNormalUserDTO, true);

        Optional<NormalProfile> existedNormalProfileOp = normalProfileRepository.findByUserId(updatedUser.getId());
        if (!existedNormalProfileOp.isPresent()) {
            logger.error("User with username {} isn't normal user.", updatedUser.getUsername());
            throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        NormalProfile existedNormalProfile = existedNormalProfileOp.get();

        NormalProfile updatedNormalProfile = updateNormalProfile(existedNormalProfile, newNormalUserDTO);
        updatedNormalProfile.setUser(updatedUser);

        normalProfileRepository.save(updatedNormalProfile);
        logger.info("Normal user with username {} updated.", updatedUser.getUsername());
        return new NormalUserDTO(updatedNormalProfile);
    }

    private NormalProfile updateNormalProfile(NormalProfile normalProfile, NormalUserDTO normalUserDTO) {
        if (normalUserDTO.idOrganization != null) {
            normalProfile.setIdOrganization(normalUserDTO.idOrganization);
        }
        if (normalUserDTO.idEmployee != null) {
            normalProfile.setIdEmployee(normalUserDTO.idEmployee);
        }
        if (normalUserDTO.idParent != null) {
            normalProfile.setIdParent(normalUserDTO.idParent);
        }
        if (normalUserDTO.school != null) {
            normalProfile.setSchool(normalUserDTO.school);
        }
        if (normalUserDTO.grade != null) {
            normalProfile.setGrade(normalUserDTO.grade);
        }
        return normalProfile;
    }

    private void checkParameterValid(NormalUserDTO normalUserDTO, boolean update) {
        int school_length = 100;

        if (normalUserDTO.idOrganization == null && !update) {
            logger.warn("Organization id cannot be null.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Organization id cannot be null.");
        }
        if (normalUserDTO.idOrganization != null) {
            if (!organizationProfileRepository.findById(normalUserDTO.idOrganization).isPresent()) {
                logger.warn("Organization id {} doesn't exists.", normalUserDTO.idOrganization);
                throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                        "Organization id " + normalUserDTO.idOrganization + " doesn't exists.");
            }
        }
        if (normalUserDTO.idEmployee != null) {
            if (!employeeProfileRepository.findById(normalUserDTO.idEmployee).isPresent()) {
                logger.warn("Employee id {} doesn't exists.", normalUserDTO.idEmployee);
                throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                        "Employee id " + normalUserDTO.idEmployee + " doesn't exists.");
            }
        }
        if (normalUserDTO.idParent != null) {
            if (!normalProfileRepository.findById(normalUserDTO.idParent).isPresent()) {
                logger.warn("Parent id {} doesn't exists.", normalUserDTO.idParent);
                throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                        "Parent id " + normalUserDTO.idParent + " doesn't exists.");
            }
        }
        if (normalUserDTO.school != null && normalUserDTO.school.length() > school_length) {
            logger.warn("The length of school name cannot be greater than {}.", school_length);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of school name cannot be greater than " + school_length + ".");
        }
    }
}
