package com.nhnacademy.booklay.booklayfront.controller.coupon;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/coupon-zone")
public class CouponZoneController {

    @GetMapping
    public String getCouponZone() {

        return "/coupon/couponZone";
    }
}
