package com.svincent7.sentraiam.common.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class BaseErrorException extends ErrorResponseException {

    public BaseErrorException(final HttpStatusCode status, final String detail) {
        this(status, ProblemDetail.forStatusAndDetail(status, detail), new RuntimeException(detail));
    }

    public BaseErrorException(final HttpStatusCode status, final Throwable cause) {
        super(status, cause);
    }

    public BaseErrorException(final HttpStatusCode status, final ProblemDetail body, final Throwable cause) {
        super(status, body, cause);
    }
}
