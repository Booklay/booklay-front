package com.nhnacademy.booklay.booklayfront.controller.mypage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.request.PointPresentRequest;
import com.nhnacademy.booklay.booklayfront.dto.member.response.PointCouponRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.PointHistoryRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.member.response.TotalPointRetrieveResponse;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * @author 양승아
 */
@Slf4j
@Controller
@RequestMapping("/point")
public class PointHistoryController {
    private final RestTemplate restTemplate;

    private final String redirectGatewayPrefix;
    private final String redirectGatewayPrefixCoupon;

    private final static String MYPAGE = "mypage/myPage";

    public PointHistoryController(RestTemplate restTemplate, String gateway) {
        this.restTemplate = restTemplate;
        this.redirectGatewayPrefix = gateway + "/shop/v1" + "/point";
        this.redirectGatewayPrefixCoupon = gateway + "/coupon/v1" + "/members";
    }

    @GetMapping("/{memberNo}")
    public String retrievePointList(@RequestParam(value = "page", defaultValue = "0") int page,
                                    @PathVariable Long memberNo,
                                    Model model) {

        String query = "?page=" + page;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/" + memberNo + query);

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        ResponseEntity<PageResponse<PointHistoryRetrieveResponse>> response =
            restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<>() {
                });

        List<PointHistoryRetrieveResponse> list = Objects.requireNonNull(response.getBody()).getData();

        model.addAttribute("pointList", list);
        model.addAttribute("targetUrl", "member/memberPointList");

        return MYPAGE;
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

    @GetMapping("/coupon/{memberNo}")
    public String retrievePointCouponList(@PathVariable Long memberNo,
                                          Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefixCoupon + "/" + memberNo + "/coupons/point");

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        ResponseEntity<PageResponse<PointCouponRetrieveResponse>> response =
            restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<>() {
                });

        List<PointCouponRetrieveResponse> list = Objects.requireNonNull(response.getBody()).getData();

        model.addAttribute("pointCouponList", list);
        model.addAttribute("memberNo", memberNo);
        model.addAttribute("targetUrl", "member/memberPointCouponList");

        return MYPAGE;
//        return "mypage/member/memberPointCouponList";
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

        restTemplate.exchange(requestEntity, Void.class);

        return "redirect:/point/" + memberNo;
    }

    @GetMapping("/coupon/{memberNo}/{couponId}")
    public String convertPointCoupon(@PathVariable Long memberNo,
                                     @PathVariable Long couponId,
                                     Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + "/" + memberNo + "/" + couponId);

        RequestEntity<Void> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, uri);

        restTemplate.exchange(requestEntity, Void.class);

        return "redirect:/point/" + memberNo;
    }
}
