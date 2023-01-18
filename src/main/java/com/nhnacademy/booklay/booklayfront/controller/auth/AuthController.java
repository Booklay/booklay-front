package com.nhnacademy.booklay.booklayfront.controller.auth;

import com.nhnacademy.booklay.booklayfront.auth.LoginRequest;
import com.nhnacademy.booklay.booklayfront.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public String doLogin(@ModelAttribute @Valid LoginRequest loginRequest, HttpServletResponse response) {

        authService.login(loginRequest);

        return "/index";
    }
}
