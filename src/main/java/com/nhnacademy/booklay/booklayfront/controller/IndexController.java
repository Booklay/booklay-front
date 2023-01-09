package com.nhnacademy.booklay.booklayfront.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final RestTemplate restTemplate;
    private final String gatewayIp;

    @ResponseBody
    @GetMapping("/shop")
    public String shop() {
        String testMember = restTemplate.getForObject( gatewayIp+"/shop/v1/members", String.class);
        return testMember;
    }

}
