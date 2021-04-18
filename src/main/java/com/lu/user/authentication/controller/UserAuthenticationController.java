package com.lu.user.authentication.controller;

import com.lu.user.authentication.exceptions.UserAuthenticationException;
import com.lu.user.authentication.exceptions.UserException;
import com.lu.user.authentication.model.User;
import com.lu.user.authentication.model.pojo.Users;
import com.lu.user.authentication.service.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.validation.Valid;
import javax.ws.rs.FormParam;
import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserAuthenticationController {

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity register(@RequestBody @Valid User user) throws UserException {
        log.info("Register user {}", user.getUsername());
        if (userService.getUser(user.getUsername()) != null) {
            log.error("User already registered");
            throw new UserException("User already registered", HttpStatus.BAD_REQUEST);
        }

        User registeredUser =  userService.registerUser(user);

        //FIXME: maybe return an id for the FE side
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity login(HttpServletRequest request, HttpServletResponse response) throws UserAuthenticationException {
        Principal principal = request.getUserPrincipal();
        if (principal == null) {
            log.error("Authentication failed.", principal.getName());
            throw new UserAuthenticationException("Authentication failed.", HttpStatus.UNAUTHORIZED);
        }
        String loginUsername = principal.getName();
        log.info("Perform user {} login", loginUsername);
        User user = userService.getUser(loginUsername);

        if (user == null) {
            log.error("User {} not found", loginUsername);
            throw new UserAuthenticationException("User not found", HttpStatus.UNAUTHORIZED);
        }

        switch (user.getState()) {
            case ACTIVE:
                log.info("User {} s is active", loginUsername);
                userService.saveLoginSuccess(loginUsername);
                log.info("User {} was successfully authenticated", loginUsername);
                return ResponseEntity.noContent().build();
            case LOCKED:
                log.error("User {} is blocked", loginUsername);
                throw new UserAuthenticationException("User is locked.", HttpStatus.UNAUTHORIZED);
            default:
                log.error("Unknown user state:" + user.getState());
                throw new IllegalArgumentException("User state unknown");
        }
    }

    @PostMapping(path = "/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.logout();
        log.info("Successful user logout");
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/userDetails", produces = "application/json")
    public ResponseEntity userDetails(@RequestParam String username) throws UserException {
        log.info("Get user {} details", username);
        User user = userService.getUser(username);
        if (user == null) {
            log.error("No user found with username {}}", username);
            throw new UserException("No user found with username " + username, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping(path = "/allUsers", produces = "application/json")
    public ResponseEntity allUsers() throws UserException {
        log.info("Get all users");
        List<User> usersList = userService.getAllUsers();
        Users users = new Users();
        if(usersList != null && !usersList.isEmpty()) {
            users.setTotalCount(usersList.size());
            users.setUsers(usersList);
        }
        return ResponseEntity.ok(users);
    }

    @PatchMapping(path = "/resetPassword", consumes = "application/json", produces = "application/json")
    public ResponseEntity resetPassword(@FormParam("username") String username,
                                        @FormParam("newPassword") String newPassword) {
        throw new UnsupportedOperationException("reset user password");
    }

    @PatchMapping(path = "/unlockUser", consumes = "application/json", produces = "application/json")
    public ResponseEntity resetPassword(@FormParam("username") String username) {
        throw new UnsupportedOperationException("unlock user");
    }

}
