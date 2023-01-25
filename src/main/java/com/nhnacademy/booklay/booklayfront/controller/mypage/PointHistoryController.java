package com.nhnacademy.booklay.booklayfront.controller.mypage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.TotalPointRetrieveResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        model.addAttribute("targetUrl", "member/memberPointPresent");

        return MYPAGE;
    }

    @PostMapping("/present/{memberNo}")
    public String pointPresent(@Valid @ModelAttribute PointPresentRequest pointPresentRequest,
                               BindingResult bindingResult,
                               @PathVariable Long memberNo,
                               Model model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI uri = URI.create(redirectGatewayPrefix + "/present/" + memberNo);

        RequestEntity<String> requestEntity =
            new RequestEntity<>(objectMapper.writeValueAsString(pointPresentRequest), headers,
                HttpMethod.POST, uri);

        ResponseEntity<Void> response =
            restTemplate.exchange(requestEntity, Void.class);

        model.addAttribute("targetUrl", "member/memberPointList");

        return MYPAGE;
    }
}
