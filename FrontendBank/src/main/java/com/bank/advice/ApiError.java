package com.bank.advice;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private final LocalDateTime timeStamp;
    private final String error;
    private final String message;
    private final int statusCode;
    private final HttpStatus status;
    private final String path;

    public ApiError(String error, String message, HttpStatus status, String path) {
        this.timeStamp = LocalDateTime.now();
        this.error = error;
        this.message = message;
        this.status = status;
        this.statusCode = status.value();
        this.path = path;
    }

}