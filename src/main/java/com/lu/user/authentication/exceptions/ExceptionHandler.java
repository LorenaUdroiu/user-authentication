package com.lu.user.authentication.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler  extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorDetails> userAuthenticationHandler(HttpServletRequest request, UserException ex) {
        if (logger.isErrorEnabled()) {
            logger.error(ex.getMessage());
        }

        ErrorDetails errorDetails = new ErrorDetails(ex.getStatus().value(), ex.getStatus().getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorDetails);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorDetails> userAuthenticationHandler(HttpServletRequest request, UserAuthenticationException ex) {
        if (logger.isErrorEnabled()) {
            logger.error(ex.getMessage());
        }

        ErrorDetails errorDetails = new ErrorDetails(ex.getStatus().value(), ex.getStatus().getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(errorDetails);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void badRequestHandler(HttpServletRequest request, IllegalArgumentException ex) {
        if (logger.isWarnEnabled()) {
            logger.error("Bad arguments while calling " + request.getPathTranslated(), ex);
        }
    }

    @org.springframework.web.bind.annotation.ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void genericHandler(HttpServletRequest request, Exception ex) {
        if (logger.isErrorEnabled()) {
            logger.error("Internal Server Error while calling " + request.getPathTranslated(), ex);
        }
    }
}
