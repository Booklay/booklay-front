package com.nhnacademy.booklay.booklayfront.auth;

import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 인증을 처리하기 위한 AuthenticationProvider 클래스입니다.
 *
 * @author 조현진
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class UsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerProxy proxy;
    private final TokenUtils tokenUtils;

    /**
     * 프록시로 인증 서버를 호출합니다.
     *
     * @param authentication - InitialAuthenticationFilter에서 만든 인증 객체입니다. name에 로그인 요청한 id, credentials에 비밀번호를 담고있습니다.
     * @return Authentication 객체를 반환합니다.
     * @throws AuthenticationException 인증 실패시 발생할 수 있는 오류입니다.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        log.info("login start , {}", username);

        JwtInfo jwtInfo = proxy.sendAuth(username, password);

        String role = tokenUtils.getRole(jwtInfo);

        return new UsernamePasswordAuthentication(jwtInfo.getUuid(), jwtInfo.getJwt(), Collections.singletonList(new SimpleGrantedAuthority(role)));
    }

    /**
     * Authentication의 UsernamePasswordAuthentication 형식을 지원할 이 AuthenticationProvider를 설계한다.
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthentication.class.isAssignableFrom(authentication);
    }



}
