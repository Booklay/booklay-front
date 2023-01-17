package com.nhnacademy.booklay.booklayfront.service;

import com.nhnacademy.booklay.booklayfront.auth.LoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final RestTemplate restTemplate;

    private final String gatewayIp;

    public void login(LoginRequest loginRequest) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<Object> response = restTemplate.exchange(gatewayIp + "/auth/v1/login", HttpMethod.POST, request, new ParameterizedTypeReference<>() {
        });

    }
}
