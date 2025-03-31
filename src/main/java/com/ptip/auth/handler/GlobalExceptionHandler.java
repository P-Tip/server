package com.ptip.auth.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<?> handleCustomExceptions(RuntimeException ex) {
        HttpStatus status;
        String error;

        if (ex instanceof UserNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            error = "USER_NOT_FOUND";
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            error = "UNKNOWN_ERROR";
        }

        return ResponseEntity
                .status(status)
                .body(Map.of("error", error, "message", ex.getMessage()));
    }
}
