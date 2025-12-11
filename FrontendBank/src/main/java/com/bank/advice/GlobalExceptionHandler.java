package com.bank.advice;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Handle Feign exceptions (API client errors)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ApiError> handleFeignException(FeignException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(exception.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ApiError apiError = new ApiError(
                status.getReasonPhrase(),
                exception.getLocalizedMessage(),
                status,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, status);
    }

    // Handle HttpClientErrorException (Spring RestTemplate/WebClient errors)
    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiError> handleHttpClientError(HttpClientErrorException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());

        ApiError apiError = new ApiError(
                status.getReasonPhrase(),
                exception.getMessage(),
                status,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, status);
    }

    // Handle IllegalArgumentException (bad input from user)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                "Invalid Argument",
                exception.getMessage(),
                HttpStatus.BAD_REQUEST,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Handle generic Exception (fallback)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception exception, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                "Internal Server Error",
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                request.getRequestURI()
        );

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}