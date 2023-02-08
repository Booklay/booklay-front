package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponMemberIssueRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponIssueResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponMemberResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon-zone")
@Slf4j
@RestController
public class CouponZoneRestController {

    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    @Value("${booklay.gw-url}")
    private String gwUrl;

    private static final String COUPON_DOMAIN_PREFIX = "/coupon/v1";
    private static final String SHOP_DOMAIN_PREFIX = "/shop/v1";

    /**
     * 사용자의 쿠폰 발급.
     *
     */
    @GetMapping("/{couponId}")
    public ResponseEntity<CouponIssueResponse> couponZoneIssue(@PathVariable Long couponId, MemberInfo memberInfo) {
        Long memberNo = memberInfo.getMemberNo();

        CouponMemberIssueRequest request = new CouponMemberIssueRequest(couponId, memberNo);
        Map<String, Object> map = objectMapper.convertValue(request, Map.class);

        URI requestToShopUrl = URI.create(gatewayIp + SHOP_DOMAIN_PREFIX + "/member/coupon-zone");

        log.info(memberInfo.getMemberId() + ", " + couponId + " :: 보냅니다.");
        ApiEntity<CouponIssueResponse>
            response = restService.post(requestToShopUrl.toString(), map, new ParameterizedTypeReference<>() {});
        log.warn(response.getBody().getRequestId());

        return response.getSuccessResponse();
    }

    /**
     * Fetch Polling
     */
    @GetMapping("/member/response/{requestId}")
    public ResponseEntity<CouponMemberResponse> couponZoneIssue(@PathVariable String requestId) {
        URI requestToShopUrl = URI.create(gatewayIp + SHOP_DOMAIN_PREFIX + "/member/coupon-zone/" + requestId);
        log.info("**********************************" + requestId);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("requestId", requestId);

        ApiEntity<CouponMemberResponse> response = restService.get(requestToShopUrl.toString(), map, new ParameterizedTypeReference<>() {});
        log.info(response.getBody().getMessage() + "******************");
        return response.getSuccessResponse();
    }
}
