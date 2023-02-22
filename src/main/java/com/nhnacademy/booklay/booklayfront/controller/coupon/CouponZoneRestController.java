package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponIssueResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponMemberResponse;
import com.nhnacademy.booklay.booklayfront.exception.CouponZoneException;
import com.nhnacademy.booklay.booklayfront.exception.LoginEssentialException;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.coupon.CouponZoneService;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/coupon-zone")
@Slf4j
@RestController
public class CouponZoneRestController {

    private final RestService restService;
    private final String gatewayIp;
    private final CouponZoneService couponZoneService;

    private static final String COUPON_DOMAIN_PREFIX = "/coupon/v1";
    private static final String SHOP_DOMAIN_PREFIX = "/shop/v1";

    /**
     * 사용자의 이달의 쿠폰 요청을 오픈 시간과 발급 만료 기간을 확인 후, shop 서버로 보냅니다.
     */
    @PostMapping("/{couponId}")
    public ResponseEntity<CouponIssueResponse> couponZoneIssue(@PathVariable Long couponId, MemberInfo memberInfo) {
        Long memberNo = memberInfo.getMemberNo();

        couponZoneService.checkTimeAndGrade(couponId, memberInfo.getMemberGrade());

        ApiEntity<CouponIssueResponse> response = couponZoneService.issueCouponAtZone(couponId, memberNo);

        return response.getSuccessResponse();
    }

    /**
     * Polling Mapping
     * 발급 요청 후에 받은 requestId로, 요청에 대한 응답 결과를 가지고 옵니다.
     */
    @GetMapping("/member/response/{requestId}")
    public ResponseEntity<CouponMemberResponse> couponZoneIssue(@PathVariable String requestId) {
        URI requestToShopUrl = URI.create(gatewayIp + SHOP_DOMAIN_PREFIX + "/member/coupon-zone/" + requestId);
        log.debug(requestId + " + come!!");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("requestId", requestId);

        ApiEntity<CouponMemberResponse> response =
            restService.get(requestToShopUrl.toString(), map, new ParameterizedTypeReference<>() {});

        return response.getSuccessResponse();
    }

    /**
     * 사용자의 등급 및 무제한 쿠폰 요청을 오픈 시간과 발급 만료 기간을 확인 후, coupon 서버로 보냅니다.
     */
    @PostMapping("/no-limit/{couponId}")
    public ResponseEntity<CouponIssueResponse> couponZoneIssueNoLimit(@PathVariable Long couponId, MemberInfo memberInfo) {
        Long memberNo = memberInfo.getMemberNo();

        log.debug(memberInfo.getMemberGrade());
        couponZoneService.checkTimeAndGrade(couponId, memberInfo.getMemberGrade());

        ApiEntity<CouponIssueResponse> response = couponZoneService.issueNoLimitCouponAtZone(couponId, memberNo);

        return response.getSuccessResponse();
    }
}
