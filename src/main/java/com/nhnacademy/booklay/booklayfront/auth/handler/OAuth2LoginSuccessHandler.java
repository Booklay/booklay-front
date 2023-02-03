package com.nhnacademy.booklay.booklayfront.auth.handler;

import com.nhnacademy.booklay.booklayfront.auth.AuthenticationServerProxy;
import com.nhnacademy.booklay.booklayfront.auth.CustomMember;
import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final RestTemplate restTemplate;

    private final AuthenticationServerProxy proxy;

    private final String gatewayIp;

    private static final String SHOP_PREFIX = "/shop/v1";


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String email = principal.getAttribute("email");
        String gitId = Objects.requireNonNull(principal.getAttribute("id")).toString();

        JwtInfo jwtInfo = proxy.attemptGithubOauth2Authentication(email, gitId);


        request.getSession().setAttribute("TOKEN", jwtInfo.getAccessToken());
        request.getSession().setAttribute("REFRESH_TOKEN", jwtInfo.getRefreshToken());

        response.sendRedirect("/");

    }
}
