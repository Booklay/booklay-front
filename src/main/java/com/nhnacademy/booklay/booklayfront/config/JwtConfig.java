package com.nhnacademy.booklay.booklayfront.config;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.DatatypeConverter;

@RequiredArgsConstructor
@Configuration
public class JwtConfig {

    @Value(("${booklay.jwt.secret}"))
    private final String secret;

    @Bean
    public JwtParser jwtParser() {
        return Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(secret))
                .build();
    }
}
