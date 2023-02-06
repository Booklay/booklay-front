package com.nhnacademy.booklay.booklayfront.auth.filter;

import com.nhnacademy.booklay.booklayfront.auth.AuthenticationServerProxy;
import com.nhnacademy.booklay.booklayfront.auth.CustomMember;
import com.nhnacademy.booklay.booklayfront.auth.constant.Roles;
import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

                accessToken = jwtInfo.getAccessToken();
            }

            String email = tokenUtils.getEmail(accessToken);
            String role = tokenUtils.getRole(accessToken);

            CustomMember customMember = new CustomMember(email, null, Collections.singletonList(Roles.valueOf(role)));
            customMember.setAccessToken(accessToken);

            UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(customMember, null, customMember.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(token);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error(e.getMessage());
            filterChain.doFilter(request, response);
        } finally {
            log.info("security context cleared");
            SecurityContextHolder.clearContext();
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
        c.add(Calendar.MINUTE, -5);

        return claims.getExpiration().before(c.getTime());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("login");
    }
}
