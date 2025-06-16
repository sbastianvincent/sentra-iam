package com.svincent7.sentraiam.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseErrorException {

    public BadRequestException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
