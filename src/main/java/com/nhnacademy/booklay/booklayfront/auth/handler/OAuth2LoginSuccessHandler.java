package com.nhnacademy.booklay.booklayfront.auth.handler;

import com.nhnacademy.booklay.booklayfront.auth.AuthenticationServerProxy;
import com.nhnacademy.booklay.booklayfront.auth.jwt.JwtInfo;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final AuthenticationServerProxy proxy;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {

        OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        String gitId = Objects.requireNonNull(principal.getAttribute("id")).toString();
        String email = principal.getAttribute("email");

        JwtInfo jwtInfo = proxy.attemptGithubOauth2Authentication(gitId, email);

        request.getSession().setAttribute("TOKEN", jwtInfo.getAccessToken());
        request.getSession().setAttribute("REFRESH_TOKEN", jwtInfo.getRefreshToken());



        response.sendRedirect("/");

    }
}
