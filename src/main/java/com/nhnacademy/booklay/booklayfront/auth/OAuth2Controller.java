package com.nhnacademy.booklay.booklayfront.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
public class OAuth2Controller {

    @Value("${booklay.oauth2.github-client-id}")
    private String clientId;

    @Value("${booklay.oauth2.github-client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    @GetMapping("/login/github")
    public String login(HttpServletResponse response) {

        response.addHeader("Authorization", "token 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");

        return "redirect:https://github.com/login/oauth/authorize?client_id=" + clientId;

    }

    // @GetMapping("/login/oauth2/code/github")
    // public String callback(@RequestParam(name = "code") String code) {
    //
    //     String url = "https://github.com/login/oauth/access_token?client_id=" + clientId + "&client_secret=" + clientSecret + "&code=" + code;
    //
    //     String accessToken = restTemplate.postForObject(url, null, String.class);
    //
    //
    //     return null;
    // }
}
