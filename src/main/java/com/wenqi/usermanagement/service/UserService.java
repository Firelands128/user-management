/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service;

import com.wenqi.usermanagement.dto.EmployeeUserDTO;
import com.wenqi.usermanagement.dto.NormalUserDTO;
import com.wenqi.usermanagement.dto.OrganizationUserDTO;
import com.wenqi.usermanagement.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    Optional<UserDTO> checkUsernameValid(String username);

    NormalUserDTO addUser(NormalUserDTO normalUserDTO, boolean oauth);

    OrganizationUserDTO addUser(OrganizationUserDTO organizationUserDTO, boolean oauth);

    EmployeeUserDTO addUser(EmployeeUserDTO employeeUserDTO, boolean oauth);

    UserDTO getByUsername(String username);

    NormalUserDTO updateUser(String username, NormalUserDTO normalUserDTO);

    OrganizationUserDTO updateUser(String username, OrganizationUserDTO organizationUserDTO);

    EmployeeUserDTO updateUser(String username, EmployeeUserDTO organizationUserDTO);

    void deleteUser(String username);
}
