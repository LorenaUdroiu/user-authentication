package com.lu.user.authentication.service;

import com.lu.user.authentication.exceptions.UserAuthenticationException;
import com.lu.user.authentication.model.User;
import com.lu.user.authentication.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Base64;

@Slf4j
@Service
public class UserService {
    private static final String BASIC_AUTH_DELIMITER = ":";

    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @param user
     */
    public void registerUser(final User user) {
        userRepository.save(user);
    }

    /**
     *
     * @param username
     */
    public User getUser(final String username) {
        return userRepository.findByUsername(username);
    }

    /**
     *
     * @param request
     * @return
     * @throws UserAuthenticationException
     */
    public Pair getUserNameAndPassword(HttpServletRequest request) throws UserAuthenticationException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String[] encodedValues = authHeader.split(" ");
        if(encodedValues == null || encodedValues.length < 2) {
            log.error("Missing authentication header");
            throw new UserAuthenticationException("Missing authentication header", HttpStatus.UNAUTHORIZED);
        }
        String encodedUsernameAndPassword = encodedValues[1];
        if(encodedUsernameAndPassword.endsWith(",")) {
            encodedUsernameAndPassword = encodedUsernameAndPassword.substring(0, encodedUsernameAndPassword.length() - 1).trim();
        }
        byte[] decodedUsernameAndPassword = Base64.getDecoder().decode(encodedUsernameAndPassword);
        String[] usernameAndPasswordValues = new String(decodedUsernameAndPassword).split(BASIC_AUTH_DELIMITER);
        if(usernameAndPasswordValues == null || usernameAndPasswordValues.length < 2) {
            log.error("Bad authentication header");
            throw new UserAuthenticationException("Bad authentication header", HttpStatus.UNAUTHORIZED);
        }
        Pair<String, String> usernameAndPassword = new ImmutablePair<>(usernameAndPasswordValues[0], usernameAndPasswordValues[1]);
        return usernameAndPassword;
    }

    /**
     *
     * @param username
     */
    public void loginSuccess(String username) {
        User user = getUser(username);
        user.setLastLogin(OffsetDateTime.now());
        userRepository.save(user);
    }

    /**
     *
     * @param username
     */
    public void loginFailed(String username) {
        User user = getUser(username);
        int failedLogins = user.getFailedLogins() + 1;
        user.setFailedLogins(failedLogins);
        userRepository.save(user);
    }
}
