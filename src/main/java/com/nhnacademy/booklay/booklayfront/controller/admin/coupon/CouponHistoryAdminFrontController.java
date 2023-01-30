package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_MEMBER_NO;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.PAGE_NUM;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.RETURN_ADMIN_PAGE;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;

import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
@Slf4j
public class CouponHistoryAdminFrontController {
    private final CouponRestApiModelSettingService couponRestApiModelSettingService;

    private static final String COUPON_HISTORY_HTML_PATH = "coupon/history/";

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }


    @GetMapping("/history/{pageNum}")
    public String historyCoupon(Model model, @PathVariable Integer pageNum) {
        couponRestApiModelSettingService.setCouponHistoryToModelByPage(pageNum, model);
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, COUPON_HISTORY_HTML_PATH + "historyView");
        return RETURN_ADMIN_PAGE;
    }

    @GetMapping("/history/{memberNo}/{pageNum}")
    public String memberHistoryCoupon(Model model, @PathVariable String memberNo,
                                      @PathVariable Integer pageNum) {
        couponRestApiModelSettingService.setCouponHistoryToModelByPageAndMemberNo(pageNum, memberNo, model);
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, COUPON_HISTORY_HTML_PATH + "historyView");
        return RETURN_ADMIN_PAGE;
    }



}
