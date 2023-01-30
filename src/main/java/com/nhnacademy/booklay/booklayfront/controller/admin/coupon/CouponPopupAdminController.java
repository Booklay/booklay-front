package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CategoryRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.MemberRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.ProductRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons/popup/")
public class CouponPopupAdminController {

    private final CouponRestApiModelSettingService couponRestApiModelSettingService;
    private final MemberRestApiModelSettingService memberRestApiModelSettingService;
    private final CategoryRestApiModelSettingService categoryRestApiModelSettingService;
    private final ProductRestApiModelSettingService productRestApiModelSettingService;
    private static final String RESOURCE_BASE = "/admin/coupon/popup/";

    @GetMapping("/coupon/pages/{pageNum}")
    public String couponPopup(@PathVariable int pageNum, Model model) {
        couponRestApiModelSettingService.setCouponListToModelByPage(pageNum, model);
        return RESOURCE_BASE + "couponPopup";
    }

    @GetMapping("/member/pages/{pageNum}")
    public String memberPopup(@PathVariable int pageNum, Model model) {
        memberRestApiModelSettingService.setMemberListToModelByPage(pageNum, model);
        return RESOURCE_BASE + "memberPopup";
    }

    @GetMapping("/category/pages/{pageNum}")
    public String categoryPopup(@PathVariable int pageNum, Model model) {
        categoryRestApiModelSettingService.setCategoryListToModelByPage(pageNum, model);
        return RESOURCE_BASE + "categoryPopup";
    }

    @GetMapping("/product/pages/{pageNum}")
    public String productPopup(@PathVariable int pageNum, Model model) {
        productRestApiModelSettingService.setProductListToModelByPage(pageNum, model);
        return RESOURCE_BASE + "productPopup";
    }
}
