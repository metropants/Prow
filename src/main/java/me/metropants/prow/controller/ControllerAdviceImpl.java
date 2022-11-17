package me.metropants.prow.controller;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ControllerAdviceImpl {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleException(@NotNull RuntimeException exception) {
        return ResponseEntity.badRequest().body(Map.of(
                "message", exception.getLocalizedMessage(),
                "status", HttpStatus.BAD_REQUEST.value()
        ));
    }

}
