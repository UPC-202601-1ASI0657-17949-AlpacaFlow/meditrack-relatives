package com.alpacaflow.meditrack.relatives.relatives.interfaces.rest;

import com.alpacaflow.meditrack.relatives.relatives.domain.exceptions.RelativeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class RelativeExceptionHandler {

    @ExceptionHandler(RelativeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRelativeNotFound(RelativeNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage(), "code", RelativeNotFoundException.CODE_NOT_FOUND));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest()
                .body(Map.of("error", ex.getMessage()));
    }
}
