package com.nhnacademy.booklay.booklayfront.auth;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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

        return "redirect:https://github.com/login/oauth/authorize?client_id=" + clientId + "&redirect_uri=http://localhost:6060/login/oauth2/code/github";

    }

}
