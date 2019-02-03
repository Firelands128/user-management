/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.controller;

import com.wenqi.usermanagement.dto.EmployeeUserDTO;
import com.wenqi.usermanagement.dto.UserDTO;
import com.wenqi.usermanagement.service.impl.GeneralUserServiceImpl;
import com.wenqi.usermanagement.utils.CheckAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(value = "/employee", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class EmployeeUserController {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GeneralUserServiceImpl userService;

    @Autowired
    public EmployeeUserController(GeneralUserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * employee user sign up with employee user DTO and
     * return a {@link ResponseEntity} of {@link EmployeeUserDTO}
     * <p>
     * URL path: "/employee"
     * </p>
     * <p>
     * HTTP method: POST
     * </p>
     *
     * @param employeeUserDTO The employee user DTO
     * @return a {@link ResponseEntity} of {@link EmployeeUserDTO}
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EmployeeUserDTO> signUp(@RequestBody EmployeeUserDTO employeeUserDTO) {
        Optional<UserDTO> userDTO = userService.checkUsernameValid(employeeUserDTO.username);
        if (userDTO.isPresent()) {
            logger.warn("Username {} have already exists.", employeeUserDTO.username);
            return new ResponseEntity<>(new EmployeeUserDTO(userDTO.get()), HttpStatus.CONFLICT);
        }
        EmployeeUserDTO addedEmployeeUserDTO = userService.addUser(employeeUserDTO, false);
        return new ResponseEntity<>(addedEmployeeUserDTO, HttpStatus.CREATED);
    }

    /**
     * employee user update with username and employee user DTO then
     * return a {@link ResponseEntity} of {@link EmployeeUserDTO}
     * <p>
     * URL path: "/employee/{username}"
     * </p>
     * <p>
     * HTTP method: PUT
     * </p>
     *
     * @param username        The username
     * @param employeeUserDTO The employee user DTO
     * @param authentication  The authentication
     * @return a {@link ResponseEntity} of {@link EmployeeUserDTO}
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{username}")
    public ResponseEntity<EmployeeUserDTO> updateEmployeeUser(@PathVariable("username") String username,
                                                              @RequestBody EmployeeUserDTO employeeUserDTO,
                                                              Authentication authentication) {
        CheckAuthentication.checkAuthentication(authentication, username);
        EmployeeUserDTO updatedEmployeeUserDTO = userService.updateUser(username, employeeUserDTO);
        return new ResponseEntity<>(updatedEmployeeUserDTO, HttpStatus.OK);
    }
}
