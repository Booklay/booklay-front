package com.nhnacademy.booklay.booklayfront.filter;

import com.nhnacademy.booklay.booklayfront.auth.UsernamePasswordAuthentication;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("Auth Filter Path = {}", request.getRequestURI());

        try {

            String jwtFormHeader = request.getHeader("Authorization");
            String jwt = getTokenFromHeader(jwtFormHeader);

            if (Objects.isNull(jwt) || jwt.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            String role = tokenUtils.getRole(jwt);
            String uuid = tokenUtils.getUuid(jwt);

            Authentication authentication =
                new UsernamePasswordAuthentication(uuid, null, Collections.singletonList(
                    new SimpleGrantedAuthority(role)));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().contains("img");
    }

    private String getTokenFromHeader(String header) {
        if (header == null ||!header.startsWith("Bearer ")) {
            return "";
        }

        return header.split(" ")[1];
    }

}
