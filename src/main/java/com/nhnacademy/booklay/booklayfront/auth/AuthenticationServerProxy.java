package com.nhnacademy.booklay.booklayfront.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationServerProxy {

    private final RestTemplate restTemplate;

    private final String gatewayIp;

    private static final String PREFIX = "/auth/v1";

    public void sendAuth(String username, String password) {

        String url = gatewayIp + PREFIX + "/members/login";

        var loginRequest = new LoginRequest(username, password);

        restTemplate.postForEntity(url, loginRequest, Void.class);
    }
}
