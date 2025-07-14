package com.svincent7.sentraiam.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidPasswordException extends BaseErrorException {

    public InvalidPasswordException() {
        super(HttpStatus.BAD_REQUEST, "Invalid Password");
    }
}
