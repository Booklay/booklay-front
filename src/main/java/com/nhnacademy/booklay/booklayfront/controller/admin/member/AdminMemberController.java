package com.nhnacademy.booklay.booklayfront.controller.admin.member;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/members")
public class AdminMemberController {

    private final RestService restService;

    private final RestTemplate restTemplate;

    private final String gatewayIp;

    private static final String PREFIX = "/shop/v1/admin/members";

    @GetMapping
    public String retrieveAllMembers() {

        ResponseEntity<PageResponse> members = restTemplate.getForEntity(gatewayIp + PREFIX, PageResponse.class);

        members.getBody();
        return "admin/member/memberList";
    }
}
