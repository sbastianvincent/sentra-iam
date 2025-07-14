package com.svincent7.sentraiam.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseErrorException {

    public UnauthorizedException(final String exception) {
        super(HttpStatus.UNAUTHORIZED, exception);
    }
}
