package com.nhnacademy.booklay.booklayfront.member.controller;

import static javax.swing.text.html.CSS.getAttribute;

import com.nhnacademy.booklay.booklayfront.coupon.domain.FrontURI;
import com.nhnacademy.booklay.booklayfront.member.domain.MemberCreateRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final RestTemplate restTemplate;
    private final FrontURI frontURI;

    @PostMapping
    public String postCreateCouponType(@ModelAttribute MemberCreateRequest memberCreateRequest,
                                       RedirectAttributes redirectAttributes) {
        HttpEntity<MemberCreateRequest> request = new HttpEntity<>(memberCreateRequest);
        ResponseEntity<Void> response = restTemplate.exchange(frontURI.SHOPURI + "/members", HttpMethod.POST, request, Void.class);

        //TODO 1: 에러처리
        if(!response.getStatusCode().equals(HttpStatus.CREATED)) {
            redirectAttributes.addFlashAttribute("error", response.getStatusCode());
            return "redirect:/error";
        }

        return "redirect:/shop";

    }
}
