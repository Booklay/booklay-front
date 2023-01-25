package com.nhnacademy.booklay.booklayfront.filter;

import com.nhnacademy.booklay.booklayfront.auth.UsernamePasswordAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value(("${booklay.jwt.secret}"))
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwtFormHeader = request.getHeader("Authorization");
        String jwt = getTokenFromHeader(jwtFormHeader);

        if (Objects.isNull(jwt)) {
            filterChain.doFilter(request, response);
            return;
        }

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        String username = String.valueOf(claims.get("username"));

        String authority = String.valueOf(claims.get("role"));

        var auth = new UsernamePasswordAuthentication(username, null, Collections.singletonList(new SimpleGrantedAuthority(authority)));
        SecurityContextHolder.getContext()
                .setAuthentication(auth);

        filterChain.doFilter(request, response);

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        return request.getServletPath()
                .equals("login");
    }

    private String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

}
