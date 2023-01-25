package com.nhnacademy.booklay.booklayfront.controller.mypage;

import com.nhnacademy.booklay.booklayfront.dto.member.response.TotalPointRetrieveResponse;
import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequestMapping("/point")
public class PointHistoryController {
    private final RestTemplate restTemplate;

    private final String redirectGatewayPrefix;

    private final static String MYPAGE = "/mypage/myPage";

    public PointHistoryController(RestTemplate restTemplate, String gateway) {
        this.restTemplate = restTemplate;
        this.redirectGatewayPrefix = gateway + "/shop/v1" + "/point";
    }

    @GetMapping("/present/{memberNo}")
    public String retrievePointPresentForm(@PathVariable Long memberNo,
                                           Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/total/" + memberNo);

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        ResponseEntity<TotalPointRetrieveResponse> response =
            restTemplate.exchange(requestEntity, TotalPointRetrieveResponse.class);

        model.addAttribute("totalPoint", response.getBody());
        return "mypage/member/memberPointPresent";
    }
}
