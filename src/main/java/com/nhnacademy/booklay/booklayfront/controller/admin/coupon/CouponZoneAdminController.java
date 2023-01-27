package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/coupon-zone")
public class CouponZoneAdminController {

    private static final String RETURN_PAGE = "admin/adminPage";

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    @GetMapping
    public String getCouponZoneList(Model model) {
        model.addAttribute(TARGET_VIEW, "/coupon/couponZone/couponZoneList");
        return RETURN_PAGE;
    }

}
