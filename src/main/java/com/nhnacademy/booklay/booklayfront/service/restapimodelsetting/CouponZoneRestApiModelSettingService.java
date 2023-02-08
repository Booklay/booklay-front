package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponZoneRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class CouponZoneRestApiModelSettingService {
    private final RestService restService;
    private final String gatewayIp;


    private static final String COUPON_DOMAIN_PREFIX = "/coupon/v1";
    private static final String SHOP_DOMAIN_PREFIX = "/shop/v1";

    public void getMemberCouponZoneList(Model model) {
        URI getLimitedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/member/coupon-zone/limited");
        URI getGradedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/member/coupon-zone/graded");
        URI getUnlimitedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/member/coupon-zone/unlimited");

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> limitedList =
            restService.get(getLimitedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> gradedList =
            restService.get(getGradedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> unlimitedList =
            restService.get(getUnlimitedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        model.addAttribute("limitedList", limitedList.getBody().getData());
        model.addAttribute("gradedList", gradedList.getBody().getData());
        model.addAttribute("unlimitedList", unlimitedList.getBody().getData());
    }
}
