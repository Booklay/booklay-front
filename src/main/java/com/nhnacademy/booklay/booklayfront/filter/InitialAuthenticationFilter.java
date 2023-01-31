package com.nhnacademy.booklay.booklayfront.filter;

import com.nhnacademy.booklay.booklayfront.auth.UsernamePasswordAuthentication;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 로그인을 시도할 때 단 한번 실행되는 필터.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitialAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;

    private static final List<String> EXCLUDE_URL =
        List.of("/login");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String memberId = request.getParameter("memberId");
        String password = request.getParameter("password");

        UsernamePasswordAuthentication authentication = new UsernamePasswordAuthentication(memberId, password);

        Authentication authenticated = authenticationManager.authenticate(authentication);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticated);
        response.addCookie(new Cookie("SESSION_ID",(String) authenticated.getPrincipal()));
        log.info(authenticated.getAuthorities().toString());
        log.info((String) authenticated.getPrincipal());

        response.sendRedirect("/");

    }

    /**
     * /login 경로에만 이 필터를 적용한다.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return EXCLUDE_URL.stream()
                          .noneMatch(exclude -> exclude.equals(request.getServletPath()));
    }
}
