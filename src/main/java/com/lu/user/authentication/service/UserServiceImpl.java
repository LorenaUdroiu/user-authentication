package com.lu.user.authentication.service;

import com.lu.user.authentication.exceptions.UserAuthenticationException;
import com.lu.user.authentication.model.Address;
import com.lu.user.authentication.model.User;
import com.lu.user.authentication.repository.AddressRepository;
import com.lu.user.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.List;

import static com.lu.user.authentication.model.UserState.ACTIVE;
import static com.lu.user.authentication.model.UserState.LOCKED;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private static final String USERNAME_PASSWORD_DELIMITER = ":";
    private static final String BASIC_AUTHENTICATION_DELIMITER = " ";
    private static final Integer LOCKED_USER_FAILED_LOGINS_LIMIT = 3;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(final User user) {
        Address address = addressRepository.save(user.getAddress());
        user.setAddress(address);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateDate(OffsetDateTime.now());
        user.setUpdateDate(OffsetDateTime.now());
        user.setState(ACTIVE);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User getUser(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll(Sort.by("username"));
    }

    @Override
    public Pair getUserNameAndPassword(String authHeader) throws UserAuthenticationException {

        if(StringUtils.isEmpty(authHeader)) {
            log.error("Missing authentication header");
            throw new UserAuthenticationException("Missing authentication header", HttpStatus.UNAUTHORIZED);
        }

        String[] encodedValues = authHeader.split(BASIC_AUTHENTICATION_DELIMITER);
        if(encodedValues == null || encodedValues.length < 2) {
            log.error("Missing authentication data");
            throw new UserAuthenticationException("Missing authentication data", HttpStatus.UNAUTHORIZED);
        }

        String encodedUsernameAndPassword = encodedValues[1];
        if(encodedUsernameAndPassword.endsWith(",")) {
            encodedUsernameAndPassword = encodedUsernameAndPassword.substring(0, encodedUsernameAndPassword.length() - 1).trim();
        }

        byte[] decodedUsernameAndPassword = Base64.getDecoder().decode(encodedUsernameAndPassword);
        String[] usernameAndPasswordValues = new String(decodedUsernameAndPassword).split(USERNAME_PASSWORD_DELIMITER);
        if(usernameAndPasswordValues == null || usernameAndPasswordValues.length < 2) {
            log.error("Bad authentication header");
            throw new UserAuthenticationException("Wrong authentication header", HttpStatus.UNAUTHORIZED);
        }

        Pair<String, String> usernameAndPassword = new ImmutablePair<>(usernameAndPasswordValues[0], usernameAndPasswordValues[1]);

        return usernameAndPassword;
    }

    @Override
    @Transactional
    public User saveLoginSuccess(String username) throws UserAuthenticationException {
        User user = getUser(username);
        if (user == null) {
            log.error("User {} not found", username);
            throw new UserAuthenticationException("User not found", HttpStatus.UNAUTHORIZED);
        }
        user.setLastLogin(OffsetDateTime.now());
        user.setUpdateDate(OffsetDateTime.now());
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User saveLoginFailed(String username) throws UserAuthenticationException {
        User user = getUser(username);
        if (user == null) {
            log.error("User {} not found", username);
            throw new UserAuthenticationException("User not found", HttpStatus.UNAUTHORIZED);
        }
        int failedLogins = user.getFailedLogins() + 1;
        if (failedLogins >= LOCKED_USER_FAILED_LOGINS_LIMIT) {
            user.setState(LOCKED);
        }
        user.setFailedLogins(failedLogins);
        user.setUpdateDate(OffsetDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void login(User user, String password) throws UserAuthenticationException{
        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.error("Authentication failed. Bad credentials.");
            saveLoginFailed(user.getUsername());
            throw new UserAuthenticationException("Authentication failed. Bad credentials.", HttpStatus.UNAUTHORIZED);
        }
    }
}
