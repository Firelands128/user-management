/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.controller;

import com.wenqi.usermanagement.dto.NormalUserDTO;
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
@RequestMapping(value = "/normal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class NormalUserController {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GeneralUserServiceImpl userService;

    @Autowired
    public NormalUserController(GeneralUserServiceImpl userService) {
        this.userService = userService;
    }

    /**
     * normal user sign up with normal user DTO and
     * return a {@link ResponseEntity} of {@link NormalUserDTO}
     * <p>
     * URL path: "/normal"
     * </p>
     * <p>
     * HTTP method: POST
     * </p>
     *
     * @param normalUserDTO The normal user DTO
     * @return a {@link ResponseEntity} of {@link NormalUserDTO}
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NormalUserDTO> signUp(@RequestBody NormalUserDTO normalUserDTO) {
        Optional<UserDTO> userDTO = userService.checkUsernameValid(normalUserDTO.username);
        if (userDTO.isPresent()) {
            logger.warn("Username {} have already exists.", normalUserDTO.username);
            return new ResponseEntity<>(new NormalUserDTO(userDTO.get()), HttpStatus.CONFLICT);
        }
        NormalUserDTO addedNormalUserDTO = userService.addUser(normalUserDTO, false);
        return new ResponseEntity<>(addedNormalUserDTO, HttpStatus.CREATED);
    }

    /**
     * normal user update with username and normal user DTO then
     * return a {@link ResponseEntity} of {@link NormalUserDTO}
     * <p>
     * URL path: "/normal/{username}"
     * </p>
     * <p>
     * HTTP method: PUT
     * </p>
     *
     * @param username       The username
     * @param normalUserDTO  The normal user DTO
     * @param authentication The authentication
     * @return a {@link ResponseEntity} of {@link NormalUserDTO}
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{username}")
    public ResponseEntity<NormalUserDTO> updateNormalUser(@PathVariable("username") String username,
                                                          @RequestBody NormalUserDTO normalUserDTO,
                                                          Authentication authentication) {
        CheckAuthentication.checkAuthentication(authentication, username);
        NormalUserDTO updatedNormalUserDTO = userService.updateUser(username, normalUserDTO);
        return new ResponseEntity<>(updatedNormalUserDTO, HttpStatus.OK);
    }
}
