package com.svincent7.sentraiam.common.exception.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.svincent7.sentraiam.common.exception.BaseErrorException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;

import java.time.Instant;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Instant timestamp;
    private String path;
    private Object error;
    private Integer status;

    public static ApiErrorResponse buildFromException(final Exception exception, final HttpStatusCode httpStatusCode,
                                                      final String path) {
        return buildApiErrorResponse(path, exception.getMessage(), httpStatusCode.value());
    }

    public static ApiErrorResponse buildFromBaseErrorException(final BaseErrorException exception, final String path) {
        ProblemDetail problemDetail = exception.getBody();
        return buildApiErrorResponse(path, problemDetail.getDetail(), exception.getStatusCode().value());
    }

    public static ApiErrorResponse buildInternalErrorResponse(final String path) {
        return buildApiErrorResponse(path, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ApiErrorResponse buildApiErrorResponse(final String path, final Object error, final int status) {
        ApiErrorResponse response = new ApiErrorResponse();
        response.setTimestamp(Instant.now());
        response.setPath(path);
        response.setError(error);
        response.setStatus(status);
        return response;
    }
}
