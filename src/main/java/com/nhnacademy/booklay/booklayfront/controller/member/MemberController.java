package com.nhnacademy.booklay.booklayfront.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.request.MemberUpdateRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.MemberRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final RestService restService;
    private final ObjectMapper objectMapper;

    private final static String MYPAGE = "mypage/myPage";

    public MemberController(RestTemplate restTemplate, RestService restService, String gateway,
                            ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.restService = restService;
        this.objectMapper = objectMapper;
        redirectGatewayPrefix = gateway + "/shop/v1" + "/members";
    }


    @PostMapping("/register")
    public String createMember(@Valid @ModelAttribute MemberCreateRequest memberCreateRequest,
                               BindingResult bindingResult) {

        URI uri = URI.create(redirectGatewayPrefix);

        restService.post(uri.toString(), objectMapper.convertValue(memberCreateRequest, Map.class),
            Void.class);
        //TODO 2: 에러처리

        return "redirect:/";
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

    @GetMapping("/{memberNo}")
    public String retrieveMemberDetail(@PathVariable Long memberNo, Model model, MemberInfo memberInfo) {
        URI uri = URI.create(redirectGatewayPrefix + "/" + memberNo);

        ApiEntity<MemberRetrieveResponse> response =
            restService.get(uri.toString(), null, MemberRetrieveResponse.class);

        model.addAttribute("member", response.getBody());
        model.addAttribute("memberNo", memberNo);
        model.addAttribute("targetUrl", "member/memberDetail");

        log.info("member id = {}", memberInfo.getMemberId());
        return MYPAGE;
    }

    @GetMapping("/update/{memberNo}")
    public String retrieveMemberUpdateForm(@PathVariable Long memberNo, Model model) {
        String query = "/" + memberNo;

        URI uri = URI.create(redirectGatewayPrefix + query);

        ApiEntity<MemberRetrieveResponse> response =
            restService.get(uri.toString(), null, MemberRetrieveResponse.class);

        if (response.isSuccess()) {
            model.addAttribute("member", response.getBody());
            model.addAttribute("memberNo", memberNo);

            return "mypage/member/memberUpdateForm";
        } else {
            return "/";
        }
    }

    @PostMapping("/update/{memberNo}")
    public String updateMember(@PathVariable Long memberNo,
                               @Valid @ModelAttribute MemberUpdateRequest request,
                               BindingResult bindingResult) {

        URI uri = URI.create(redirectGatewayPrefix + "/" + memberNo);

        restService.put(uri.toString(), objectMapper.convertValue(request, Map.class),
            Void.class);

        return "redirect:/members/" + memberNo;
    }

    @GetMapping("/drop/{memberNo}")
    public String retrieveDeleteMemberForm(@PathVariable Long memberNo, Model model) {
        model.addAttribute("memberNo", memberNo);

        return "mypage/member/memberDeleteForm";
    }

    @GetMapping("/drop/process/{memberNo}")
    public String deleteMember(@PathVariable Long memberNo) {
        URI uri = URI.create(redirectGatewayPrefix + "/" + memberNo);

        restService.delete(uri.toString(), null);

        return "redirect:/";
    }
}
