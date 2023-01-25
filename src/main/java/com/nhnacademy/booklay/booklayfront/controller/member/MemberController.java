package com.nhnacademy.booklay.booklayfront.controller.member;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequestMapping("/members")
public class MemberController {
    private final RestTemplate restTemplate;

    private final String redirectGatewayPrefix;

    private final static String MYPAGE = "/mypage/myPage";

    public MemberController(RestTemplate restTemplate, String gateway) {
        this.restTemplate = restTemplate;
        redirectGatewayPrefix = gateway + "/shop/v1" + "/members";
    }

    @GetMapping("/login")
    public String retrieveLoginForm() {
        return "member/loginForm";
    }

    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String retrieveCreateMemberForm(Model model) {
        return "member/createMemberForm";
    }

    @PostMapping("/register")
    public String createMember(@Valid @ModelAttribute MemberCreateRequest memberCreateRequest,
                               BindingResult bindingResult)
        throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<String> requestEntity =
            new RequestEntity<>(objectMapper.writeValueAsString(memberCreateRequest), httpHeaders,
                HttpMethod.POST, URI.create(redirectGatewayPrefix));

        restTemplate.exchange(requestEntity, Void.class);
        //TODO 2: 에러처리

        return "redirect:/";
    }

    @GetMapping("/{memberNo}")
    public String retrieveMemberDetail(@PathVariable Long memberNo, Model model) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/" + memberNo);

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(httpHeaders, HttpMethod.GET, uri);

        ResponseEntity<MemberRetrieveResponse> response
            = restTemplate.exchange(requestEntity, MemberRetrieveResponse.class);

        model.addAttribute("member", response.getBody());
        model.addAttribute("memberNo", memberNo);
        model.addAttribute("targetUrl", "member/memberDetail");

        return "mypage/member/memberDetail";
    }

}
