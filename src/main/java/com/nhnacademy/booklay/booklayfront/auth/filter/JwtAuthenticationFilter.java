package com.nhnacademy.booklay.booklayfront.auth.filter;

import com.nhnacademy.booklay.booklayfront.auth.AuthenticationServerProxy;
import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private final AuthenticationServerProxy proxy;

    private static final String ACCESS_TOKEN_HEADER = "TOKEN";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        log.info("JwtAuthenticationFilter Path = {}", request.getRequestURI());

        try {
            if (isEmptyAccessToken(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            HttpSession session = request.getSession();

            String accessToken = (String) session.getAttribute(ACCESS_TOKEN_HEADER);

            if (isTokenNotValid(accessToken)) {
                log.info("Access Token 만료로 인한 토큰 최신화");

                String refreshToken = (String) request.getSession().getAttribute("REFRESH_TOKEN");
                JwtInfo jwtInfo = proxy.refreshAccessToken(refreshToken, accessToken);

                session.setAttribute(ACCESS_TOKEN_HEADER, jwtInfo.getAccessToken());

            }

        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            log.info("Security Context Remove");
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            response.sendRedirect("/");
        }

    }

    private boolean isEmptyAccessToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (Objects.isNull(session) || Objects.isNull(session.getAttribute(ACCESS_TOKEN_HEADER))) {
            return true;
        }

        return false;
    }

    private boolean isTokenNotValid(String jwt) {
        Claims claims = tokenUtils.getClaims(jwt);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 5);

        return claims.getExpiration().after(c.getTime());
    }
}
