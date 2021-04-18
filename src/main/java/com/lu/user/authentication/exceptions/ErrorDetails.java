package com.lu.user.authentication.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@AllArgsConstructor
@Data
@EqualsAndHashCode
public class ErrorDetails {
    private int status;
    private String error;
    private String message;
}
