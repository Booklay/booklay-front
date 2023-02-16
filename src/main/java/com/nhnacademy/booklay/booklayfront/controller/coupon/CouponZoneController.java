package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
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
     * 사용자의 이달의 쿠폰 페이지를 조회합니다.
     * isBlind = false 인 쿠폰을 보여줍니다.
     */
    @GetMapping
    public String getCouponZone(Model model, MemberInfo memberInfo) {
        model.addAttribute("memberInfo", memberInfo);
        couponZoneService.getMemberCouponZoneLimitedList(model);

        return "coupon/couponZone";
    }

    /**
     * 사용자의 등급 및 무제한 쿠폰 페이지를 조회합니다.
     * isBlind = false 인 쿠폰을 보여줍니다.
     */
    @GetMapping("/no-limit")
    public String getCouponZoneNoLimit(Model model, MemberInfo memberInfo) {
        model.addAttribute("memberInfo", memberInfo);
        couponZoneService.getMemberCouponZoneUnlimitedList(model);

        return "coupon/couponZoneNoLimit";
    }
}
