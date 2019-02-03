/*
 * Created by Wenqi Li <Firelands128@gmail.com>
 * Copyright (C) 2019.
 */

package com.wenqi.usermanagement.service.impl;

import com.wenqi.usermanagement.constants.RoleEnum;
import com.wenqi.usermanagement.constants.UserStatus;
import com.wenqi.usermanagement.constants.UserType;
import com.wenqi.usermanagement.dao.entity.User;
import com.wenqi.usermanagement.dao.entity.UserRole;
import com.wenqi.usermanagement.dao.repository.UserRepository;
import com.wenqi.usermanagement.dao.repository.UserRoleRepository;
import com.wenqi.usermanagement.dto.EmployeeUserDTO;
import com.wenqi.usermanagement.dto.NormalUserDTO;
import com.wenqi.usermanagement.dto.OrganizationUserDTO;
import com.wenqi.usermanagement.dto.UserDTO;
import com.wenqi.usermanagement.exception.ErrorCode;
import com.wenqi.usermanagement.exception.MyException;
import com.wenqi.usermanagement.service.UserService;
import com.wenqi.usermanagement.utils.EmailAndPhoneValidator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class GeneralUserServiceImpl implements UserService {
    // The application logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final NormalUserService normalUserService;
    private final OrganizationUserService organizationUserService;
    private final EmployeeUserService employeeUserService;

    @Autowired
    public GeneralUserServiceImpl(UserRepository userRepository,
                                  UserRoleRepository userRoleRepository,
                                  PasswordEncoder passwordEncoder,
                                  NormalUserService normalUserService,
                                  OrganizationUserService organizationUserService,
                                  EmployeeUserService employeeUserService) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.normalUserService = normalUserService;
        this.organizationUserService = organizationUserService;
        this.employeeUserService = employeeUserService;
    }

    /**
     * check username valid when click on username check button and
     * return an {@link Optional} of {@link UserDTO}
     *
     * @param username The username needed to be check
     * @return an {@link Optional} of {@link UserDTO}
     */
    @Override
    public Optional<UserDTO> checkUsernameValid(String username) {
        if (username == null) {
            logger.warn("Username cannot be null.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Username cannot be null.");
        }
        if (username.length() > 32) {
            logger.warn("Username is too long");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Username is too long");
        }
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            UserDTO returnUser = new UserDTO();
            returnUser.username = username;
            returnUser.email = createHiddenValue(user.get().getEmail());
            returnUser.phone = createHiddenValue(user.get().getPhone());
            returnUser.userType = user.get().getUserType();
            logger.info("Username {} have already exists.", username);
            return Optional.of(returnUser);
        }
        return Optional.empty();
    }

    @Override
    public NormalUserDTO addUser(NormalUserDTO normalUserDTO, boolean oauth) {
        User user = this.addUser(new User(normalUserDTO), oauth);
        return normalUserService.addUser(user, normalUserDTO, oauth);
    }

    @Override
    public OrganizationUserDTO addUser(OrganizationUserDTO organizationUserDTO, boolean oauth) {
        User user = this.addUser(new User(organizationUserDTO), oauth);
        return organizationUserService.addUser(user, organizationUserDTO, oauth);
    }

    @Override
    public EmployeeUserDTO addUser(EmployeeUserDTO employeeUserDTO, boolean oauth) {
        User user = this.addUser(new User(employeeUserDTO), oauth);
        return employeeUserService.addUser(user, employeeUserDTO, oauth);
    }

    protected User addUser(User user, boolean oauth) {
        User addedUser;
        if (!oauth) {
            this.checkParametersValid(user, false);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            addedUser = userRepository.save(user);
        } else {
            Optional<User> addedUserOp = userRepository.findByUsername(user.getUsername());
            if (!addedUserOp.isPresent()) {
                logger.error("User with username {} doesn't exists. Add user with oauth user failed.", user.getUsername());
                throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            addedUser = addedUserOp.get();
            addedUser.setUserType(user.getUserType());
            userRepository.save(addedUser);
        }
        return addedUser;
    }

    @Override
    public UserDTO getByUsername(String username) {
        User user = getUserByUsername(username);
        long userId = user.getId();
        UserType userType = user.getUserType();
        switch (userType) {
            case NORMAL:
                Optional<NormalUserDTO> normalUserDTO = normalUserService.getByUserId(userId);
                if (!normalUserDTO.isPresent()) {
                    logger.error("Query normal profile failed. User with username {} exists.", username);
                    throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
                return normalUserDTO.get();
            case ORGANIZATION:
                Optional<OrganizationUserDTO> organizationUserDTO = organizationUserService.getByUserId(userId);
                if (!organizationUserDTO.isPresent()) {
                    logger.error("Query organization profile failed. User with username {} exists.", username);
                    throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
                return organizationUserDTO.get();
            case EMPLOYEE:
                Optional<EmployeeUserDTO> employeeUserDTO = employeeUserService.getByUserId(userId);
                if (!employeeUserDTO.isPresent()) {
                    logger.error("Query employee profile failed. User with username {} exists.", username);
                    throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
                }
                return employeeUserDTO.get();
        }
        logger.error("User type incorrect. Get user info by username failed.");
        throw new MyException(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    protected User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            logger.warn("User with username {} doesn't exists.", username);
            throw new MyException(ErrorCode.NOT_FOUND, "User with username " + username + " doesn't exists.");
        }
        return user.get();
    }

    @Override
    public NormalUserDTO updateUser(String username, NormalUserDTO normalUserDTO) {
        User user = getUserByUsername(username);
        User updatedUser = this.updateUser(user, normalUserDTO);
        return normalUserService.updateUser(updatedUser, normalUserDTO);
    }

    @Override
    public OrganizationUserDTO updateUser(String username, OrganizationUserDTO organizationUserDTO) {
        User user = getUserByUsername(username);
        User updatedUser = this.updateUser(user, organizationUserDTO);
        return organizationUserService.updateUser(updatedUser, organizationUserDTO);
    }

    @Override
    public EmployeeUserDTO updateUser(String username, EmployeeUserDTO employeeUserDTO) {
        User user = getUserByUsername(username);
        User updatedUser = this.updateUser(user, employeeUserDTO);
        return employeeUserService.updateUser(updatedUser, employeeUserDTO);
    }

    @Override
    public void deleteUser(String username) {
        userRepository.updateUserStatus(username, UserStatus.DELETED);
    }

    /**
     * check parameters valid when add or update user, if invalid throw exception.
     *
     * @param user The {@link User}
     * @param update  Boolean value to indicate whether it's used for adding or updating.
     */
    private void checkParametersValid(User user, boolean update) {
        if (user.getStatus().equals(UserStatus.UNREGISTERED)) {
            return;
        }
        if (checkUsernameValid(user.getUsername()).isPresent()) {
            logger.warn("Username {} have already exists.", user.getUsername());
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Username " + user.getUsername() + " have already exists.");
        }
        if (user.getPhone() == null && user.getEmail() == null && !update) {
            logger.warn("Phone and email at least one contains.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Phone and email at least one contains.");
        }
        if (user.getPhone() != null) {
            checkPhoneValid(user.getPhone());
        }
        if (user.getEmail() != null) {
            checkEmailValid(user.getEmail());
        }
        checkNameValid(user.getName());
        checkAvatarUrlValid(user.getAvatarUrl());
        checkAreaValid(user.getArea(), update);
        checkAddressValid(user.getAddress());
        checkCountyValid(user.getCounty());
        checkCityValid(user.getCity());
        checkProvinceValid(user.getProvince());
        checkCountryValid(user.getCountry());
        checkOthersValid(user.getOthers());
    }

    private Set<UserRole> updateUserRole(User user, Set<RoleEnum> roleSet) {
        if (roleSet == null || roleSet.size() <= 0) {
            Set<UserRole> userRoles = userRoleRepository.findByUser(user);
            if (userRoles == null || userRoles.size() <= 0) {
                logger.warn("There must be at least 1 role for {} user with username {}",
                        user.getUserType().toString().toLowerCase(),
                        user.getUsername());
                throw new MyException(ErrorCode.BAD_REQUEST, "There must be at least 1 role for " +
                        user.getUserType().toString().toLowerCase() +
                        " user with username " +
                        user.getUsername());
            }
            return userRoles;
        }
        Set<UserRole> returnUserRoles = new HashSet<>();
        Set<UserRole> userRoles = userRoleRepository.findByUser(user);
        if (userRoles == null || userRoles.size() <= 0) {
            logger.error("there is no roles for user {}", user);
            roleSet.forEach(role -> returnUserRoles.add(new UserRole(user, role)));
            return returnUserRoles;
        }

        Set<RoleEnum> rolesToDelete = new HashSet<>(userRoles.size());
        userRoles.forEach(userRole -> rolesToDelete.add(userRole.getRole()));
        rolesToDelete.removeAll(new HashSet<>(roleSet));

        // delete unused user role
        for (RoleEnum roleEnum : rolesToDelete) {
            Optional<UserRole> userRole = userRoleRepository.findByUserAndRole(user, roleEnum);
            if (!userRole.isPresent()) {
                logger.error("Unknown Exception");
                continue;
            }
            user.getUserRoles().remove(userRole.get());
            userRoleRepository.delete(userRole.get());
        }

        for (RoleEnum role : roleSet) {
            Optional<UserRole> userRole = userRoleRepository.findByUserAndRole(user, role);
            if (userRole.isPresent()) {
                returnUserRoles.add(userRole.get());
            } else {
                returnUserRoles.add(new UserRole(user, role));
            }
        }
        return returnUserRoles;
    }

    private User updateUser(User user, UserDTO userDTO) {
        Set<UserRole> userRoles;
        if (userDTO.roles != null) {
            userRoles = updateUserRole(user, new HashSet<>(userDTO.roles));
        } else {
            userRoles = user.getUserRoles();
        }
        user.setUserRoles(userRoles);

        if (userDTO.username != null) {
            user.setUsername(userDTO.username);
        }
        if (userDTO.password != null) {
            user.setPassword(passwordEncoder.encode(userDTO.password));
        }
        if (userDTO.status != null) {
            user.setStatus(userDTO.status);
        }
        if (userDTO.userType != null) {
            user.setUserType(userDTO.userType);
        }
        if (userDTO.phone != null) {
            user.setPhone(userDTO.phone);
        }
        if (userDTO.email != null) {
            user.setEmail(userDTO.email);
        }
        if (userDTO.name != null) {
            user.setName(userDTO.name);
        }
        if (userDTO.avatarUrl != null) {
            user.setAvatarUrl(userDTO.avatarUrl);
        }
        if (userDTO.gender != null) {
            user.setGender(userDTO.gender);
        }
        if (userDTO.birthday != null) {
            user.setBirthday(userDTO.birthday);
        }
        if (userDTO.area != null) {
            user.setArea(userDTO.area);
        }
        if (userDTO.address != null) {
            user.setAddress(userDTO.address);
        }
        if (userDTO.country != null) {
            user.setCountry(userDTO.country);
        }
        if (userDTO.city != null) {
            user.setCity(userDTO.city);
        }
        if (userDTO.province != null) {
            user.setCountry(userDTO.province);
        }
        if (userDTO.others != null) {
            user.setOthers(userDTO.others);
        }
        userRepository.save(user);
        return user;
    }

    private String createHiddenValue(String original) {
        return createHiddenValue(original, 3);
    }

    private String createHiddenValue(String original, int numOfShownCharacter) {
        if (null == original) return null;
        int length = original.length();
        if (length <= numOfShownCharacter * 3) {
            return createHiddenValue(original, numOfShownCharacter - 1);
        }
        return StringUtils.substring(original, 0, numOfShownCharacter) + "***" + StringUtils.substring(original, length - numOfShownCharacter, length);
    }

    private void checkPhoneValid(String phone) {
        assert phone != null;
        if (!EmailAndPhoneValidator.phoneValidate(phone)) {
            logger.warn("The phone number given is not valid.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The phone number given is not valid.");
        }
        if (userRepository.findByPhone(phone).isPresent()) {
            logger.warn("Phone {} have already existed.", phone);
            throw new MyException(ErrorCode.CONFLICT, "Phone " + phone + " have already existed.");
        }
    }

    private void checkEmailValid(String email) {
        assert email != null;
        if (!EmailAndPhoneValidator.emailValidate(email)) {
            logger.warn("The email address given is not valid.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The email address given is not valid.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            logger.warn("Email {} have already existed.", email);
            throw new MyException(ErrorCode.CONFLICT, "Email " + email + " have already existed.");
        }
    }

    private void checkNameValid(String name) {
        if (name == null) {
            return;
        }
        int nameLength = 64;
        if (name.length() > nameLength) {
            logger.warn("The length of name cannot be greater than {}.", nameLength);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of name cannot be greater than " + nameLength + ".");
        }
    }

    private void checkAvatarUrlValid(String avatarUrl) {
        if (avatarUrl == null) {
            return;
        }
        int avatarLength = 200;
        if (avatarUrl.length() > avatarLength) {
            logger.warn("The length of name cannot be greater than {}.", avatarLength);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of name cannot be greater than " + avatarLength + ".");
        }
    }

    private void checkAreaValid(String area, boolean update) {
        int area_length = 100;
        if (area == null && !update) {
            logger.warn("Area cannot be null.");
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY, "Area cannot be null.");
        }
        if (area != null && area.length() > area_length) {
            logger.warn("The length of area cannot be greater than {}.", area_length);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of area cannot be greater than " + area_length + ".");
        }
    }

    private void checkAddressValid(String address) {
        if (address == null) {
            return;
        }
        int addressLength = 100;
        if (address.length() > addressLength) {
            logger.warn("The length of address cannot be greater than {}.", addressLength);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of address cannot be greater than " + addressLength + ".");
        }
    }

    private void checkCountyValid(String county) {
        if (county == null) {
            return;
        }
        int countyLength = 32;
        if (county.length() > countyLength) {
            logger.warn("The length of county cannot be greater than {}.", countyLength);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of county cannot be greater than " + countyLength + ".");
        }
    }

    private void checkCityValid(String city) {
        if (city == null) {
            return;
        }
        int cityLength = 32;
        if (city.length() > cityLength) {
            logger.warn("The length of city cannot be greater than {}.", cityLength);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of city cannot be greater than " + cityLength + ".");
        }
    }

    private void checkProvinceValid(String province) {
        if (province == null) {
            return;
        }
        int provinceLength = 32;
        if (province.length() > provinceLength) {
            logger.warn("The length of province cannot be greater than {}.", provinceLength);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of province cannot be greater than " + provinceLength + ".");
        }
    }

    private void checkCountryValid(String country) {
        if (country == null) {
            return;
        }
        int countryLength = 32;
        if (country.length() > countryLength) {
            logger.warn("The length of country cannot be greater than {}.", countryLength);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of country cannot be greater than " + countryLength + ".");
        }
    }

    private void checkOthersValid(String others) {
        if (others == null) {
            return;
        }
        int othersLength = 45;
        if (others.length() > othersLength) {
            logger.warn("The length of others cannot be greater than {}.", othersLength);
            throw new MyException(ErrorCode.UNPROCESSABLE_ENTITY,
                    "The length of others cannot be greater than " + othersLength + ".");
        }
    }
}
