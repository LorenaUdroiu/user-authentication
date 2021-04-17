package com.lu.user.authentication.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler  extends ResponseEntityExceptionHandler {

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
