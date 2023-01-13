package com.nhnacademy.booklay.booklayfront.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final RestTemplate restTemplate;
//    private final FrontURI frontURI;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        return "member/createMemberForm";
    }

//    @PostMapping
//    public String postCreateCouponType(@ModelAttribute MemberCreateRequest memberCreateRequest,
//                                       RedirectAttributes redirectAttributes) {
//        HttpEntity<MemberCreateRequest> request = new HttpEntity<>(memberCreateRequest);
//
//        /**
//         * localhost로 돌아가는 코드
//         * 후에 삭제
//         */
//        ResponseEntity<Void> response =
//            restTemplate.exchange("http://localhost:9090" + "/members", HttpMethod.POST, request,
//                Void.class);

//        ResponseEntity<Void> response =
//            restTemplate.exchange(frontURI.SHOP_URI + "/members", HttpMethod.POST, request,
//                Void.class);
//
//        //TODO 1: 에러처리
//        if (!response.getStatusCode().equals(HttpStatus.CREATED)) {
//            redirectAttributes.addFlashAttribute("error", response.getStatusCode());
//            return "redirect:/error";
//        }
//
//        return "redirect:/shop";
//
//    }


    @PostMapping("/memberLogin")
    @ResponseStatus(HttpStatus.OK)
    public String memberLogin() {

        return "redirect:/shop";
    }
}
