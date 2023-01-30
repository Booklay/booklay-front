package com.nhnacademy.booklay.booklayfront.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenUtils {

    private final JwtParser parser;

    public Claims getClaims(JwtInfo jwtInfo) {
        return parser.parseClaimsJws(jwtInfo.getJwt()).getBody();
    }

    public String getRole(JwtInfo jwtInfo) {
        Claims claims = getClaims(jwtInfo);

        var role =
            (ArrayList<LinkedHashMap<String, String>>) claims.get("role");

        return role.get(0).get("authority");
    }
}
