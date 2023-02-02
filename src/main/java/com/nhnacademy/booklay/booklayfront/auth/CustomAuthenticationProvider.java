package com.nhnacademy.booklay.booklayfront.auth;

import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationServerProxy proxy;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(AuthenticationServerProxy proxy, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.proxy = proxy;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

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

        CustomMember customMember = (CustomMember) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, customMember.getPassword())) {
            throw new BadCredentialsException(customMember.getUsername() + "Invalid password");
        }

        customMember.setJwt(jwtInfo.getAccessToken());
        customMember.setUuid(jwtInfo.getUuid());

        return new UsernamePasswordAuthenticationToken(customMember, customMember.getPassword(), customMember.getAuthorities());
    }

    /**
     * Authentication의 UsernamePasswordAuthentication 형식을 지원할 이 AuthenticationProvider를 설계한다.
     *
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
