package com.nhnacademy.booklay.booklayfront.auth.filter;

import com.nhnacademy.booklay.booklayfront.auth.AuthenticationServerProxy;
import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenUtils tokenUtils;
    private final AuthenticationServerProxy proxy;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        log.info("JwtAuthenticationFilter Path = {}", request.getRequestURI());

        try {
            HttpSession session = request.getSession(false);
            String accessToken = (String) session.getAttribute("TOKEN");

            if (Objects.isNull(accessToken)) {
                filterChain.doFilter(request, response);
            }

            if (isTokenNotValid(accessToken)) {
                log.info("Access Token 만료로 인한 토큰 최신화");

                String refreshToken = (String) request.getSession().getAttribute("REFRESH_TOKEN");
                JwtInfo jwtInfo = proxy.refreshAccessToken(refreshToken, accessToken);

                session.setAttribute("TOKEN", jwtInfo.getAccessToken());

            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    private boolean isTokenNotValid(String jwt) {
        Claims claims = tokenUtils.getClaims(jwt);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 5);

        return claims.getExpiration().after(c.getTime());
    }
}
