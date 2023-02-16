package com.nhnacademy.booklay.booklayfront.service.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponZoneMemberIssueRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponIssueResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponZoneCheckResponse;
import com.nhnacademy.booklay.booklayfront.dto.grade.Grade;
import com.nhnacademy.booklay.booklayfront.exception.CouponZoneException;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponZoneService {

    private final RestService restService;
    private final String gatewayIp;
    private final ObjectMapper objectMapper;

    private static final String COUPON_DOMAIN_PREFIX = "/coupon/v1";
    private static final String SHOP_DOMAIN_PREFIX = "/shop/v1";

    public void checkTimeAndGrade(Long couponId, String memberGrade) {
        URI checkTimeUrl = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/member/coupon-zone/" + couponId);

        ApiEntity<CouponZoneCheckResponse>
            checkResponse = restService.get(checkTimeUrl.toString(), null, new ParameterizedTypeReference<>() {});

        CouponZoneCheckResponse response = checkResponse.getBody();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime issuanceDeadlineAt = response.getIssuanceDeadlineAt();
        LocalDateTime openedAt = response.getOpenedAt();

        if(now.isBefore(openedAt) || now.isAfter(issuanceDeadlineAt)) {
            throw new CouponZoneException("발급 가능한 시간이 아닙니다.");
        }

        String grade = response.getGrade();
        if(!grade.equals(Grade.ANY.getKorGrade())) {
            if(!grade.equals(memberGrade)) {
                throw new CouponZoneException("발급 대상이 아닙니다.");
            }
        }
    }

    public ApiEntity<CouponIssueResponse> issueCouponAtZone(Long couponId, Long memberNo) {
        URI requestToShopUrl = URI.create(gatewayIp + SHOP_DOMAIN_PREFIX + "/member/coupon-zone");

        CouponZoneMemberIssueRequest request = new CouponZoneMemberIssueRequest(couponId, memberNo);
        Map<String, Object> map = objectMapper.convertValue(request, Map.class);

        return restService.post(requestToShopUrl.toString(), map, new ParameterizedTypeReference<>() {});
    }

}
