package com.lu.user.authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserAuthenticationException extends Exception {
    private String message;
    private HttpStatus status;
    //private int code;
}
