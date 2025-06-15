package com.svincent7.sentraiam.common.exception.handler;

import com.svincent7.sentraiam.common.exception.BaseErrorException;
import com.svincent7.sentraiam.common.exception.HashingException;
import com.svincent7.sentraiam.common.exception.InvalidPasswordException;
import com.svincent7.sentraiam.common.exception.ResourceNotFoundException;
import com.svincent7.sentraiam.common.exception.UnauthorizedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class BaseExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse handleInvalidArgument(final MethodArgumentNotValidException exception,
                                                  final HttpServletRequest request) {
        Map<String, Object> validationMap = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error -> {
            validationMap.put(error.getField(), error.getDefaultMessage());
        });
        return ApiErrorResponse.buildApiErrorResponse(request.getRequestURI(),
                validationMap, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidPasswordException.class)
    public ApiErrorResponse handleInvalidPasswordException(final BaseErrorException exception,
                                                            final HttpServletRequest request) {
        return ApiErrorResponse.buildFromBaseErrorException(exception, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ApiErrorResponse handleIllegalArgumentException(final IllegalArgumentException exception,
                                                           final HttpServletRequest request) {
        return ApiErrorResponse.buildFromException(exception, HttpStatus.BAD_REQUEST, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public ApiErrorResponse handleUnauthorized(final BaseErrorException exception,
                                               final HttpServletRequest request) {
        return ApiErrorResponse.buildFromBaseErrorException(exception, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiErrorResponse handleResourceNotFoundException(final BaseErrorException exception,
                                                            final HttpServletRequest request) {
        return ApiErrorResponse.buildFromBaseErrorException(exception, request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HashingException.class)
    public ApiErrorResponse handleHashingException(final HashingException exception,
                                                   final HttpServletRequest request) {
        return ApiErrorResponse.buildFromException(exception, HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiErrorResponse handleHttpMessageNotReadableException(final HttpMessageNotReadableException exception,
                                                   final HttpServletRequest request) {
        return ApiErrorResponse.buildFromException(exception, HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiErrorResponse handleDataIntegrityViolationException(final DataIntegrityViolationException exception,
                                                                  final HttpServletRequest request) {
        String errorTitle = "Data Integrity Violation";
        log.error(errorTitle, exception);

        // print stacktrace
        Writer buffer = new StringWriter();
        PrintWriter pw = new PrintWriter(buffer);
        exception.printStackTrace(pw);
        log.error(buffer.toString());

        return ApiErrorResponse.buildInternalErrorResponse(request.getRequestURI());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ApiErrorResponse handleAllUncaughtException(final Exception exception,
                                                       final HttpServletRequest request) {
        String errorTitle = "Unknown error occurred";
        log.error(errorTitle, exception);

        // print stacktrace
        Writer buffer = new StringWriter();
        PrintWriter pw = new PrintWriter(buffer);
        exception.printStackTrace(pw);
        log.error(buffer.toString());

        return ApiErrorResponse.buildInternalErrorResponse(request.getRequestURI());
    }
}
