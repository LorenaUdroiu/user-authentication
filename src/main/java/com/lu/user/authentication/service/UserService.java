package com.lu.user.authentication.service;

import com.lu.user.authentication.exceptions.UserAuthenticationException;
import com.lu.user.authentication.model.Address;
import com.lu.user.authentication.model.User;

import java.util.List;

public interface UserService {

    /**
     * Register a new user
     * @param user the user details
     * @return
     */
    public User registerUser(final User user);

    /**
     * Gets users details
     *  e.g.: First Name, Last Name, {@link Address} etc.
     * @param username
     * @return user entity
     */
    public User getUser(final String username);

    /**
     * Gets all the registered users
     * @return a list of all users
     */
    public List<User> getAllUsers();

    /**
     * Performs success login operations and persist them
     * e.g.: set the last login timestamp
     * @param username of the login user
     */
    public User saveLoginSuccess(String username) throws UserAuthenticationException;

    /**
     * Performs failed login operations and persist them
     * e.g.: increase failed logins
     * @param username of the login user
     */
    public User saveLoginFailed(String username) throws UserAuthenticationException;
}
