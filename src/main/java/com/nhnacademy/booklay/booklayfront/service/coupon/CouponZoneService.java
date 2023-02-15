package com.nhnacademy.booklay.booklayfront.service.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponZoneMemberIssueRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponIssueResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponZoneTimeResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponZoneService {

    private final RestService restService;
    private final String gatewayIp;
    private final ObjectMapper objectMapper;

    private static final String COUPON_DOMAIN_PREFIX = "/coupon/v1";
    private static final String SHOP_DOMAIN_PREFIX = "/shop/v1";

    public boolean checkTimeAtZone(Long couponId) {
        URI checkTimeUrl = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/member/coupon-zone/" + couponId);

        ApiEntity<CouponZoneTimeResponse>
            timeResponse = restService.get(checkTimeUrl.toString(), null, new ParameterizedTypeReference<>() {});

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime issuanceDeadlineAt = timeResponse.getBody().getIssuanceDeadlineAt();
        LocalDateTime openedAt = timeResponse.getBody().getOpenedAt();

        return !now.isBefore(openedAt) && !now.isAfter(issuanceDeadlineAt);
    }

    public ApiEntity<CouponIssueResponse> issueCouponAtZone(Long couponId, Long memberNo) {
        URI requestToShopUrl = URI.create(gatewayIp + SHOP_DOMAIN_PREFIX + "/member/coupon-zone");

        CouponZoneMemberIssueRequest request = new CouponZoneMemberIssueRequest(couponId, memberNo);
        Map<String, Object> map = objectMapper.convertValue(request, Map.class);

        return restService.post(requestToShopUrl.toString(), map, new ParameterizedTypeReference<>() {});
    }
}
