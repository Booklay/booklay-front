package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import com.nhnacademy.booklay.booklayfront.exception.BooklayServerException;
import com.nhnacademy.booklay.booklayfront.exception.CouponZoneTimeException;
import com.nhnacademy.booklay.booklayfront.exception.LoginEssentialException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class WebControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CouponZoneTimeException.class)
    public ResponseEntity<Object> handleCouponZoneTimeException(Exception ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }

    @ExceptionHandler(LoginEssentialException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(Exception ex) {
        Map<String, Object> result = new HashMap<>();
        result.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String notSupportedMethodRequest(MethodNotAllowedException e){
        return "redirect:/index";
    }

    @ExceptionHandler(BooklayClientException.class)
    public ResponseEntity<Object> handelBooklayClientException(BooklayClientException e) {
        HttpStatus httpStatus = HttpStatus.valueOf(e.getMessage());
        return new ResponseEntity<>(httpStatus);
    }

    @ExceptionHandler(BooklayServerException.class)
    public ResponseEntity<Object> handelBooklayServerException(BooklayServerException e) {
        HttpStatus httpStatus = HttpStatus.valueOf(e.getMessage());
        return ResponseEntity.status(httpStatus).body("{\"message\":\"잠시 뒤에 시도해주세요. 문제가 계속되면 사이트 소유자에게 문의하세요.\"}");
    }
}
