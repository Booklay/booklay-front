package com.nhnacademy.booklay.booklayfront.filter;

import com.nhnacademy.booklay.booklayfront.auth.UsernamePasswordAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String memberId = request.getParameter("memberId");
        String password = request.getParameter("password");

        UsernamePasswordAuthentication authentication = new UsernamePasswordAuthentication(memberId, password);
        authenticationManager.authenticate(authentication);

    }

    /**
     * /login 경로에만 이 필터를 적용한다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
