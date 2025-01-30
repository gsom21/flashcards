package com.gsom21.flashcards.controller.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gsom21.flashcards.exception.StatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

class Error {
    public Date timestamp;
    public int status;
    public String error;

    public Error(String error, int status) {
        this.timestamp = new Date();
        this.status = status;
        this.error = error;
    }
}

@ControllerAdvice
public class ExceptionAdvice {
    @Autowired
    public ObjectMapper mapper;

    @ExceptionHandler(StatusException.class)
    public ResponseEntity<String> handleException(StatusException e) throws JsonProcessingException {
        String body = mapper.writeValueAsString(new Error(e.getMessage(), e.getStatus().value()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(body, headers, e.getStatus());
    }
}
