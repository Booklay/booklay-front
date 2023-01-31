package com.nhnacademy.booklay.booklayfront.filter;

import com.nhnacademy.booklay.booklayfront.auth.UsernamePasswordAuthentication;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
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

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("Auth Filter Path = {}", request.getRequestURI());

        try {
            String sessionId = getSessionId(request.getCookies());
            if (Objects.isNull(sessionId)) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = (String)redisTemplate.opsForHash().get(sessionId, "TOKEN");
            String role = tokenUtils.getRole(token);
            String uuid = tokenUtils.getUuid(token);

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

    private String getSessionId(Cookie[] cookies) {

        if (Objects.isNull(cookies)) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (Objects.equals("SESSION_ID", cookie.getName())) {
                return cookie.getValue();
            }

        }
        return null;
    }

}
