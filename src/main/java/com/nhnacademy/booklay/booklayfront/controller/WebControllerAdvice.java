package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.exception.BooklayClientException;
import com.nhnacademy.booklay.booklayfront.exception.BooklayServerException;
import com.nhnacademy.booklay.booklayfront.exception.CouponZoneException;
import com.nhnacademy.booklay.booklayfront.exception.LoginEssentialException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Slf4j
@ControllerAdvice
public class WebControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CouponZoneException.class, LoginEssentialException.class})
    public ResponseEntity<Object> handleCouponZoneTimeException(Exception exception, HttpServletRequest request) {
        printLoggingError(exception, request);
        Map<String, Object> result = new HashMap<>();
        result.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(result);
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String notSupportedMethodRequest(MethodNotAllowedException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        printLoggingError(exception, request);
        redirectAttributes.addFlashAttribute("alertMessage", exception.getMessage());
        return "redirect:/index";
    }

    @ExceptionHandler(BooklayClientException.class)
    public String handelBooklayClientException(BooklayClientException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        printLoggingError(exception, request);
        redirectAttributes.addFlashAttribute("alertMessage", exception.getMessage());
        return "redirect:" + request.getHeader("Referer");
    }

    @ExceptionHandler(BooklayServerException.class)
    public String handelBooklayServerException(HttpServletRequest request, BooklayServerException exception, RedirectAttributes redirectAttributes) {
        printLoggingError(exception, request);
        redirectAttributes.addFlashAttribute("alertMessage" ,"INTERNAL_SERVER_ERROR. 문제가 계속되면 사이트 소유자에게 문의하세요");

        return "redirect:/index";
    }

    @ExceptionHandler(Exception.class)
    public String handleUnhandledException(HttpServletRequest request, Exception exception, RedirectAttributes redirectAttributes){
        printLoggingError(exception, request);
        redirectAttributes.addFlashAttribute("alertMessage" ,"알 수 없는 예외가 발생하였습니다.");

        return "redirect:/index";
    }

    private void printLoggingError(Exception exception, HttpServletRequest request) {
        log.error("\nError Occurred"
            + "\nException  : {}"
            + "\nRequest URI  : {}"
            + "\nMessage : {} ",
            exception.getClass(), request.getHeader("Referer"), exception.getMessage());
    }
}
