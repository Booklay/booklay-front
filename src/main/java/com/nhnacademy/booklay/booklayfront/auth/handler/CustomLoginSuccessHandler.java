package com.nhnacademy.booklay.booklayfront.auth.handler;

import com.nhnacademy.booklay.booklayfront.auth.CustomMember;
import com.nhnacademy.booklay.booklayfront.event.MemberEventPublisher;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final MemberEventPublisher memberEventPublisher;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {

        CustomMember principal = (CustomMember) authentication.getPrincipal();

        String accessToken = principal.getAccessToken();
        String refreshToken = principal.getRefreshToken();

        request.getSession().setAttribute("TOKEN", accessToken);
        request.getSession().setAttribute("REFRESH_TOKEN", refreshToken);

        memberEventPublisher.publishMemberLoggedIn(principal.getUsername());
        response.sendRedirect("/");
    }
}
