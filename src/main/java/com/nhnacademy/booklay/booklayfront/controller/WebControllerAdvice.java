package com.nhnacademy.booklay.booklayfront.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WebControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handelIllegalArgumentException(IllegalArgumentException e) {
        System.out.println(e.getMessage());
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
