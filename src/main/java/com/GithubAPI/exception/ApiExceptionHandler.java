package com.GithubAPI.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ApiExceptionHandler {
    private final ObjectMapper objectMapper;
    private final static String WRONG_ACCEPT_HEADER_VALUE_MSG =
            "Only acceptable 'Accept' header value is " + MediaType.APPLICATION_JSON_VALUE;

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(
                new ApiException(
                        HttpStatus.NOT_FOUND.value(),
                        e.getMessage()
                ),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotAcceptableException()
            throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(objectMapper.writeValueAsString(
                        new ApiException(
                                HttpStatus.NOT_ACCEPTABLE.value(),
                                WRONG_ACCEPT_HEADER_VALUE_MSG
                        )
                ));
    }

}
