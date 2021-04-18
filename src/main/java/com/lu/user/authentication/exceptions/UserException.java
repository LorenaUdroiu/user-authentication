package com.lu.user.authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class UserException extends Exception {
    protected String message;
    protected HttpStatus status;
    //FIXME add error code
}
