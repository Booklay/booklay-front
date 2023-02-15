package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.RETURN_ADMIN_PAGE;

import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 쿠폰 사용 기록에 대한 조회 요청을 받습니다.
 * @author 김승혜
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
@Slf4j
public class CouponHistoryAdminFrontController {
    private final CouponRestApiModelSettingService couponRestApiModelSettingService;

    private static final String ADMIN_COUPON_RESOURCE = "admin/coupon/";

    @GetMapping("/history")
    public String historyCoupon(@RequestParam(value = "page", defaultValue = "0") int pageNum) {
        return RETURN_ADMIN_PAGE;
    }
}
