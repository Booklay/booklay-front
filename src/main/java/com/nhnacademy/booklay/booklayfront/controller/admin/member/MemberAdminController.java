package com.nhnacademy.booklay.booklayfront.controller.admin.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/admin/members")
public class MemberAdminController {
    private final String redirectGatewayPrefix;
    private final RestService restService;
    private final ObjectMapper objectMapper;

    public MemberAdminController(String gateway, RestService restService,
                                 ObjectMapper objectMapper) {
        this.redirectGatewayPrefix = gateway + "/shop/v1" + "/admin/members";
        this.restService = restService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ModelAndView retrieveMemberList(@RequestParam(value = "page", defaultValue = "0") int page,
                                            ModelAndView mav) {
        String query = "?page=" + page;

        URI uri = URI.create(redirectGatewayPrefix + query);

        ApiEntity<PageResponse<MemberRetrieveResponse>> response =
            restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
            });

        if (response.isSuccess()) {
            mav.addObject("members", response.getBody().getData());
            mav.addObject("totalPage", response.getBody().getTotalPages());
            mav.addObject("currentPage", response.getBody().getPageNumber());
            mav.setViewName("admin/member/memberList");
        } else {
            mav.setViewName("/");
        }

        return mav;
    }
}
