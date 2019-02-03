/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.dao.entity.ResetPasswordToken;
import com.wenqi.usermanagement.dao.entity.User;
import com.wenqi.usermanagement.dao.repository.ResetPasswordTokenRepository;
import com.wenqi.usermanagement.dao.repository.UserRepository;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class ResetPasswordService {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${token.expiration.length.minutes}")
    private int tokenExpirationLengthInMinutes;

    @Autowired
    public ResetPasswordService(UserRepository userRepository,
                                ResetPasswordTokenRepository resetPasswordTokenRepository,
                                PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void forgetPasswordRequestByEmail(String email) {

    }

    public void forgetPasswordRequestByPhone(String phone) {
        //TODO send phone message
    }

    public Optional<ResetPasswordToken> findByToken(String token) {
        return resetPasswordTokenRepository.findByToken(token);
    }

    public ResetPasswordToken createResetPasswordTokenByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (!user.isPresent()) {
            logger.warn("Fail to find user by email {}.", email);
            throw new MyException(ErrorCode.NOT_FOUND, "Fail to find user by email " + email + ".");
        }
        return createResetPasswordToken(user.get());
    }

    public ResetPasswordToken createResetPasswordTokenByPhone(String phone) {
        Optional<User> user = userRepository.findByPhone(phone);
        if (!user.isPresent()) {
            logger.warn("Fail to find user by phone {}.", phone);
            throw new MyException(ErrorCode.NOT_FOUND, "Fail to find user by phone " + phone + ".");
        }
        return createResetPasswordToken(user.get());
    }

    private ResetPasswordToken createResetPasswordToken(User user) {
        ResetPasswordToken resetPasswordToken = null;
        String token = UUID.randomUUID().toString();
        long now = new Date().getTime();
        Timestamp expireTime = new Timestamp(now + TimeUnit.MINUTES.toMillis(tokenExpirationLengthInMinutes));
        resetPasswordToken = new ResetPasswordToken(user, token, expireTime);
        resetPasswordToken = resetPasswordTokenRepository.save(resetPasswordToken);
        return resetPasswordToken;
    }

    public void updateUserPassword(long userId, String password) {
        userRepository.updateUserPassword(userId, passwordEncoder.encode(password));
    }
}
