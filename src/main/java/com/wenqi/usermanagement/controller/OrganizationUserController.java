/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.controller;

import com.wenqi.usermanagement.dto.OrganizationUserDTO;
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
@RequestMapping(value = "/organization", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class OrganizationUserController {
	// The application logger
	private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GeneralUserServiceImpl userService;

	@Autowired
	public OrganizationUserController(GeneralUserServiceImpl userService) {
        this.userService = userService;
	}

	/**
	 * organization user sign up with organization user DTO and
	 * return a {@link ResponseEntity} of {@link OrganizationUserDTO}
	 * <p>
	 * URL path: "/organization"
	 * </p>
	 * <p>
	 * HTTP method: POST
	 * </p>
	 *
	 * @param organizationUserDTO The organization user DTO
	 * @return a {@link ResponseEntity} of {@link OrganizationUserDTO}
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<OrganizationUserDTO> signUp(@RequestBody OrganizationUserDTO organizationUserDTO) {
        Optional<UserDTO> userDTO = userService.checkUsernameValid(organizationUserDTO.username);
		if (userDTO.isPresent()) {
			logger.warn("Username {} have already exists.", organizationUserDTO.username);
			return new ResponseEntity<>(new OrganizationUserDTO(userDTO.get()), HttpStatus.CONFLICT);
		}
		OrganizationUserDTO addedOrganizationUserDTO = userService.addUser(organizationUserDTO, false);
        return new ResponseEntity<>(addedOrganizationUserDTO, HttpStatus.CREATED);
	}

	/**
	 * organization user update with username and organization user DTO then
	 * return a {@link ResponseEntity} of {@link OrganizationUserDTO}
	 * <p>
	 * URL path: "/organization/{username}"
	 * </p>
	 * <p>
	 * HTTP method: PUT
	 * </p>
	 *
	 * @param username            The username
	 * @param organizationUserDTO The organization user DTO
	 * @param authentication      The authentication
	 * @return a {@link ResponseEntity} of {@link OrganizationUserDTO}
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{username}")
	public ResponseEntity<OrganizationUserDTO> updateOrganizationUser(@PathVariable("username") String username,
																	  @RequestBody OrganizationUserDTO organizationUserDTO,
																	  Authentication authentication) {
		CheckAuthentication.checkAuthentication(authentication, username);
		OrganizationUserDTO updatedOrganizationUserDTO = userService.updateUser(username, organizationUserDTO);
		return new ResponseEntity<>(updatedOrganizationUserDTO, HttpStatus.OK);
	}
}
