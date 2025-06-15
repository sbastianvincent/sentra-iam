package com.svincent7.sentraiam.common.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseErrorException {

    public ResourceNotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
