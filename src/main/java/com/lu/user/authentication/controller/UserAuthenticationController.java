package com.lu.user.authentication.controller;

import com.lu.user.authentication.exceptions.UserAuthenticationException;
import com.lu.user.authentication.model.User;
import com.lu.user.authentication.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSessionListener;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserAuthenticationController implements HttpSessionListener {
    @Autowired
    private UserService userService;

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity register(@RequestBody User user) {
        throw new UnsupportedOperationException("register user");
    }

    @PostMapping(path = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity login(HttpServletRequest request, HttpServletResponse response) throws UserAuthenticationException {
        //FIXME do a logout to make sure no other auth objects in place
        //Basic authentication: get username and password from authentication request header
        Pair<String, String> userNameAndPassword = userService.getUserNameAndPassword(request);
        User user = userService.getUser(userNameAndPassword.getLeft());
        if(user == null) {
            log.error("User not found");
            throw new UserAuthenticationException("User not found", HttpStatus.UNAUTHORIZED);
        }
        switch (user.getState()) {
            case ACTIVE:
                log.info("User is active");
                return ResponseEntity.noContent().build(); //return token here
            case LOCKED:
                log.warn("User is locked");
                throw new UserAuthenticationException("User is locked.", HttpStatus.UNAUTHORIZED);
            default: throw new IllegalArgumentException("User state unknown");
        }
    }

    @PostMapping(path = "/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity logout() {
        throw new UnsupportedOperationException("logout user");
    }

    @GetMapping(path = "/userDetails", produces = "application/json")
    public ResponseEntity userDetails(@RequestParam String username) {
        User user = userService.getUser(username);
        return ResponseEntity.ok(user);
    }

    @PatchMapping(path = "/resetPassword", consumes = "application/json", produces = "application/json")
    public ResponseEntity resetPassword() {
        throw new UnsupportedOperationException("reset user password");
    }
}
