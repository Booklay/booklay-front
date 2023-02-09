package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponZoneRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/coupon-zone")
@Slf4j
public class CouponZoneController {

    private final CouponZoneRestApiModelSettingService couponZoneService;

    /**
     * 사용자의 쿠폰존 페이지 조회.
     * isBlind = false 인 쿠폰을 보여줍니다.
     */
    @GetMapping
    public String getCouponZone(Model model) {
        couponZoneService.getMemberCouponZoneList(model);

        return "coupon/couponZone";
    }

}
