package com.nhnacademy.booklay.booklayfront.controller;

import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/test")
public class TestController {
    private final RestTemplate restTemplate;
    private final String gatewayIp;

    @GetMapping
    public String test() {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        URI uri = URI.create(gatewayIp+"/coupon/v1/test");

        RequestEntity<Object> entity = new RequestEntity<>(HttpMethod.GET, uri);

        ResponseEntity<Map> exchange = restTemplate.exchange(entity, Map.class);

        return "/admin/coupon/empty";
    }
}
