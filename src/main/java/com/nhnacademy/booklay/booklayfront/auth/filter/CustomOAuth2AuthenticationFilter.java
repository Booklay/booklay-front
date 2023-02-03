package com.nhnacademy.booklay.booklayfront.auth.filter;

import com.nhnacademy.booklay.booklayfront.auth.AuthenticationServerProxy;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationFilter extends OncePerRequestFilter{

    private final RestTemplate restTemplate;

    private final AuthenticationServerProxy proxy;

    private final String gatewayIp;

    private static final String SHOP_PREFIX = "/shop/v1";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2AuthenticationToken) {
            setAuthentication();
        }

        filterChain.doFilter(request, response);

    }

    private void setAuthentication() {
        // SecurityContext context = SecurityContextHolder.getContext();
        // Authentication authentication = context.getAuthentication();
        // OAuth2User principal = (OAuth2User) authentication.getPrincipal();
        // String email = (String) principal.getAttribute("email");
        //
        // String url = gatewayIp + SHOP_PREFIX + "/members/email/" + email;
        //
        // MemberResponse memberResponse = restTemplate.getForObject(url, MemberResponse.class);
        //
        // JwtInfo jwtInfo = proxy.sendAuth(memberResponse.getUserId(), memberResponse.getPassword());
        //
        // CustomMember customMember =
        //     new CustomMember(memberResponse.getEmail(), memberResponse.getPassword(),
        //                      Collections.singletonList(memberResponse.getAuthority()));
        //
        // customMember.setAccessToken(jwtInfo.getAccessToken());
        // customMember.setUuid(jwtInfo.getUuid());
        // customMember.setRefreshToken(jwtInfo.getRefreshToken());
        //
        // context.setAuthentication(new UsernamePasswordAuthenticationToken(customMember, customMember.getPassword(), customMember.getAuthorities()));

    }
}
