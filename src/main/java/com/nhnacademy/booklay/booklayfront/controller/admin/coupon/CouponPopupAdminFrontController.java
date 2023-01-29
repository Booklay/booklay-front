package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.MemberRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
@Slf4j
public class CouponPopupAdminFrontController {
    private final CouponRestApiModelSettingService couponRestApiModelSettingService;
    private final MemberRestApiModelSettingService memberRestApiModelSettingService;

    @GetMapping("/popup/pages/{pageNum}")
    public String couponPopup(@PathVariable Integer pageNum, Model model) {
        couponRestApiModelSettingService.setCouponListToModelByPage(pageNum, model);
        return "/admin/coupon/couponPopup";
    }

    @GetMapping("/member/popup/pages/{pageNum}")
    public String memberPopup(@PathVariable Integer pageNum, Model model) {
        memberRestApiModelSettingService.setMemberListToModelByPage(pageNum, model);
        return "/admin/coupon/memberPopup";
    }


}
