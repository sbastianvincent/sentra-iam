package com.svincent7.sentraiam.common.exception;

import org.springframework.http.HttpStatus;

public class URINotFoundException extends BaseErrorException {

    public URINotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
