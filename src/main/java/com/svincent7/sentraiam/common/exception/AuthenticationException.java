package com.svincent7.sentraiam.common.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseErrorException {

    public AuthenticationException(final String exception) {
        super(HttpStatus.UNAUTHORIZED, exception);
    }
}
