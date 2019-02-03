/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.dao.entity.EmployeeProfile;
import com.wenqi.usermanagement.dao.entity.User;
import com.wenqi.usermanagement.dao.repository.EmployeeProfileRepository;
import com.wenqi.usermanagement.dao.repository.OrganizationProfileRepository;
import com.wenqi.usermanagement.dto.EmployeeUserDTO;
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
public class EmployeeUserService {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final EmployeeProfileRepository employeeProfileRepository;
    private final OrganizationProfileRepository organizationProfileRepository;

    @Autowired
    public EmployeeUserService(EmployeeProfileRepository employeeProfileRepository,
                               OrganizationProfileRepository organizationProfileRepository) {
        this.employeeProfileRepository = employeeProfileRepository;
        this.organizationProfileRepository = organizationProfileRepository;
    }

    protected EmployeeUserDTO addUser(User user, EmployeeUserDTO employeeUserDTO, boolean oauth) {
        checkParameterValid(employeeUserDTO, false);
        EmployeeProfile employeeProfile = new EmployeeProfile(employeeUserDTO);
        employeeProfile.setUser(user);
        employeeProfile = employeeProfileRepository.save(employeeProfile);
        logger.info("Employee User with username {} created.", employeeProfile.getUser().getUsername());
        return new EmployeeUserDTO(employeeProfile);
    }

    protected Optional<EmployeeUserDTO> getByUserId(long userId) {
        Optional<EmployeeProfile> employeeProfile = employeeProfileRepository.findByUserId(userId);
        return employeeProfile.map(EmployeeUserDTO::new);
    }

    protected EmployeeUserDTO updateUser(User updatedUser, EmployeeUserDTO employeeUserDTO) {
        checkParameterValid(employeeUserDTO, true);

        Optional<EmployeeProfile> existedEmployeeProfileOp = employeeProfileRepository.findByUserId(updatedUser.getId());
        if (!existedEmployeeProfileOp.isPresent()) {
            logger.error("User with username {} isn't employee user.", updatedUser.getUsername());
            throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
        EmployeeProfile existedEmployeeProfile = existedEmployeeProfileOp.get();

        EmployeeProfile updatedEmployeeProfile = updateEmployeeProfile(existedEmployeeProfile, employeeUserDTO);
        updatedEmployeeProfile.setUser(updatedUser);

        employeeProfileRepository.save(updatedEmployeeProfile);
        logger.info("Employee user with username {} updated.", updatedUser.getUsername());
        return new EmployeeUserDTO(updatedEmployeeProfile);
    }

    private EmployeeProfile updateEmployeeProfile(EmployeeProfile employeeProfile, EmployeeUserDTO employeeUserDTO) {
        if (employeeUserDTO.idOrganization != null) {
            employeeProfile.setIdOrganization(employeeUserDTO.idOrganization);
        }
        return employeeProfile;
    }

    private void checkParameterValid(EmployeeUserDTO employeeUserDTO, boolean update) {
        if (employeeUserDTO.idOrganization == null && !update) {
            logger.warn("Organization id cannot be null.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Organization id cannot be null.");
        }
        if (employeeUserDTO.idOrganization != null) {
            if (!organizationProfileRepository.findById(employeeUserDTO.idOrganization).isPresent()) {
                logger.warn("Organization id {} doesn't exists.", employeeUserDTO.idOrganization);
                throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                        "Organization id " + employeeUserDTO.idOrganization + " doesn't exists.");
            }
        }
    }
}
