package com.nhnacademy.booklay.booklayfront.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.auth.CustomMember;
import com.nhnacademy.booklay.booklayfront.auth.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper mapper;

    public AuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper mapper) {
        super(authenticationManager);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            log.info("start login");
//            log.info("uri = {}", request.getRequestURI());

            LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken token =
                    new UsernamePasswordAuthenticationToken(loginRequest.getMemberId(), loginRequest.getPassword());

            Authentication authentication = getAuthenticationManager().authenticate(token);

            return authentication;

        } catch (IOException e) {
            log.error("잘못된 로그인 요청: {}", e.getMessage());

            throw new IllegalAccessError("잘못된 로그인 요청입니다.");

        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {


        log.info("로구인 성공");
        CustomMember principal = (CustomMember) authResult.getPrincipal();
        log.info(principal.getUsername());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        log.error("로그인 실패: {}", failed.toString());
        getFailureHandler().onAuthenticationFailure(request, response, failed);
    }
}
