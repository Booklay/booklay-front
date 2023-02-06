package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponMemberIssueRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponZoneRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon-zone")
@Slf4j
public class CouponZoneController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    private static final String COUPON_DOMAIN_PREFIX = "/coupon/v1";
    private static final String SHOP_DOMAIN_PREFIX = "/shop/v1";

    /**
     * 사용자의 쿠폰존 페이지.
     *
     */
    @GetMapping
    public String getCouponZone(Model model) {
        URI getLimitedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/member/coupon-zone/limited");
        URI getUnlimitedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/member/coupon-zone/unlimited");

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> limitedList =
            restService.get(getLimitedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> unlimitedList =
            restService.get(getUnlimitedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        model.addAttribute("limitedList", limitedList.getBody().getData());
        model.addAttribute("unlimitedList", unlimitedList.getBody().getData());

        return "coupon/couponZone";
    }

    /**
     * 사용자의 쿠폰 발급.
     *
     */
    @GetMapping("/{couponId}")
    public String couponZoneIssue(@PathVariable Long couponId) {
//        Long memberNo = memberInfo.getMemberNo();
        Long memberNo = 3L;

        CouponMemberIssueRequest request = new CouponMemberIssueRequest(couponId, memberNo);
        Map<String, Object> map = objectMapper.convertValue(request, Map.class);

        URI requestToShopUrl = URI.create(gatewayIp + SHOP_DOMAIN_PREFIX + "/member/coupon-zone");

        restService.put(requestToShopUrl.toString(), map, String.class);

        return "redirect:/coupon-zone";
    }
}
