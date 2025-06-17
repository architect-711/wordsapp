package edu.architect_711.wordsapp.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        System.out.println("RuntimeException: " + e.getMessage());
        return new ResponseEntity<>(new ErrorResponse(
                e.getMessage(),
                e.getLocalizedMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private String message;
        private String localizedMessage;

        @Override
        public String toString() {
            return "ErrorResponse [message=" + message + ", localizedMessage=" + localizedMessage + "]";
        }
    }
}
