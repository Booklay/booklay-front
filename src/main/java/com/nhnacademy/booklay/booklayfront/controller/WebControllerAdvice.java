package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import com.nhnacademy.booklay.booklayfront.exception.BooklayServerException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class WebControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handelIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    @ExceptionHandler(BooklayClientException.class)
    public ResponseEntity<Object> handelBooklayClientException(BooklayClientException e) {
        HttpStatus httpStatus = HttpStatus.valueOf(e.getMessage());
        return new ResponseEntity<>(httpStatus);
    }

    @ExceptionHandler(BooklayServerException.class)
    public ResponseEntity<Object> handelBooklayServerException(BooklayServerException e) {
        HttpStatus httpStatus = HttpStatus.valueOf(e.getMessage());
        return new ResponseEntity<>(httpStatus);
    }
}
