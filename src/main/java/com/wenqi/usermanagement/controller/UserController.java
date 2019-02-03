/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.controller;

import com.wenqi.usermanagement.dao.entity.ResetPasswordToken;
import com.wenqi.usermanagement.dao.entity.User;
import com.wenqi.usermanagement.dto.UserDTO;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import com.wenqi.usermanagement.service.EmailService;
import com.wenqi.usermanagement.service.PhoneService;
import com.wenqi.usermanagement.service.impl.GeneralUserServiceImpl;
import com.wenqi.usermanagement.service.impl.ResetPasswordService;
import com.wenqi.usermanagement.utils.CheckAuthentication;
import com.wenqi.usermanagement.utils.EmailAndPhoneValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class UserController {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final GeneralUserServiceImpl userService;
    private final EmailService emailService;
    private final ResetPasswordService resetPasswordService;
    private final PhoneService phoneService;

    @Autowired
    public UserController(GeneralUserServiceImpl userService,
                          EmailService emailService,
                          ResetPasswordService resetPasswordService,
                          PhoneService phoneService) {
        this.userService = userService;
        this.emailService = emailService;
        this.resetPasswordService = resetPasswordService;
        this.phoneService = phoneService;
    }

    /**
     * check username exists or not,
     * if exists, return {@link HttpStatus}
     * <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html#CONFLICT">CONFLICT</a>
     * and partially hidden user info.
     * if not, return http ok.
     * <p>
     * URL path: "/user/check/{username}"
     * </p>
     * <p>
     * HTTP method: GET
     * </p>
     *
     * @param username The username
     * @return a {@link ResponseEntity} with OK or CONFLICT
     */
    @RequestMapping(method = RequestMethod.GET, value = "/check/{username}")
    public ResponseEntity<UserDTO> checkUsernameExist(@PathVariable("username") String username) {
        Optional<UserDTO> userDTO = userService.checkUsernameValid(username);
        if (userDTO.isPresent()) {
            logger.info("Username {} have already exists.", username);
            return new ResponseEntity<>(userDTO.get(), HttpStatus.CONFLICT);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * user query with username and
     * return a {@link ResponseEntity} of {@link UserDTO}
     * <p>
     * URL path: "/user/{username}"
     * </p>
     * <p>
     * HTTP method: GET
     * </p>
     *
     * @param username       The username
     * @param authentication The authentication
     * @return a {@link ResponseEntity} of {@link UserDTO}
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{username}")
    public ResponseEntity<UserDTO> getNormalUser(@PathVariable("username") String username,
                                                 Authentication authentication) {
        CheckAuthentication.checkAuthentication(authentication, username);
        UserDTO userDTO = userService.getByUsername(username);
        return ResponseEntity.ok(userDTO);
    }

    /**
     * make forget password request with email or phone number and
     * return a {@link ResponseEntity} without response body
     * <p>
     *     URL path: "/user/forget-password"
     * </p>
     * <p>
     *     HTTP method: POST
     * </p>
     * @param emailOrPhone The identifier with parameter name "identifier"
     * @return a {@link ResponseEntity} without response body
     */
    @RequestMapping(method = RequestMethod.POST, value = "/forget-password")
    public ResponseEntity forgetPasswordRequest(@RequestParam("identifier") String emailOrPhone) {
        if (!EmailAndPhoneValidator.emailValidate(emailOrPhone) && !EmailAndPhoneValidator.phoneValidate(emailOrPhone)) {
            logger.warn("The given identifier is not valid email nor phone");
            throw new MyException(ErrorCode.BAD_REQUEST, "The given identifier is not valid email nor phone");
        }
        if (EmailAndPhoneValidator.emailValidate(emailOrPhone)) {
            ResetPasswordToken token = resetPasswordService.createResetPasswordTokenByEmail(emailOrPhone);
            emailService.sendResetPasswordEmail(token.getToken(), emailOrPhone);
        }
        if (EmailAndPhoneValidator.phoneValidate(emailOrPhone)) {
            ResetPasswordToken token = resetPasswordService.createResetPasswordTokenByPhone(emailOrPhone);
            phoneService.sendSMS(token.getToken(), emailOrPhone);
        }
        return ResponseEntity.ok().build();
    }

    /**
     * modify user password with reset token and new password and
     * return a {@link ResponseEntity} without response body
     * <p>
     *     URL path: "/user/reset-password"
     * </p>
     * <p>
     *     HTTP method: POST
     * </p>
     * @param token The reset password token
     * @param password The new password
     * @return a {@link ResponseEntity} without response body
     */
    @RequestMapping(method = RequestMethod.POST, value = "/reset-password")
    public ResponseEntity resetPasswordRequest(@RequestParam("token") String token,
                                               @RequestParam("password") String password) {
        Optional<ResetPasswordToken> resetPasswordToken = resetPasswordService.findByToken(token);
        if (!resetPasswordToken.isPresent()) {
            logger.warn("The given reset password token doesn't exists.");
            throw new MyException(ErrorCode.BAD_REQUEST, "The given reset password token doesn't exists.");
        }
        if (resetPasswordToken.get().getExpireTime().before(new Date())) {
            logger.warn("The given reset password token have been expired.");
            throw new MyException(ErrorCode.BAD_REQUEST, "The given reset password token have been expired.");
        }
        User user = resetPasswordToken.get().getUser();
        resetPasswordService.updateUserPassword(user.getId(), password);
        return ResponseEntity.ok().build();
    }

    /**
     * user cancelled by username and
     * return a {@link ResponseEntity}
     * <p>
     * URL path: "/user/{username}"
     * </p>
     * <p>
     * HTTP method: DELETE
     * </p>
     *
     * @param username       The username
     * @param authentication The authentication
     * @return a {@link ResponseEntity} without response body
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{username}")
    public ResponseEntity deleteNormalUser(@PathVariable("username") String username,
                                           Authentication authentication) {
        CheckAuthentication.checkAuthentication(authentication, username);
        userService.deleteUser(username);
        return ResponseEntity.ok().build();
    }
}
