package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
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
import org.springframework.web.bind.annotation.RequestParam;


/**
 * 팝업창을 보여주기 위한 컨트롤러.
 */

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons/popup/")
public class CouponPopupAdminController {

    private final CouponRestApiModelSettingService couponRestApiModelSettingService;
    private final MemberRestApiModelSettingService memberRestApiModelSettingService;
    private final CategoryRestApiModelSettingService categoryRestApiModelSettingService;
    private final ProductRestApiModelSettingService productRestApiModelSettingService;
    private static final String RESOURCE_BASE = "admin/coupon/popup/";

    @GetMapping("/coupon")
    public String couponPopup(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                              Model model) {
        couponRestApiModelSettingService.setCouponListToModelByPage(pageNum, model);
        return RESOURCE_BASE + "couponPopup";
    }

    @GetMapping("/coupon/order")
    public String orderOrderCouponPopup(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                        @RequestParam(value = "isDuplicable") Boolean dupl,
                                        Model model, MemberInfo memberInfo){
        couponRestApiModelSettingService.setOrderCouponListToModelByPageAndMemberNo(pageNum, dupl, memberInfo, model);
        model.addAttribute("targetId", "ordercoupon");
        model.addAttribute("productNo", "order");
        model.addAttribute("isDuplicable", dupl);
        return RESOURCE_BASE + "couponPopupForOrder";
    }
    @GetMapping("/coupon/{productNo}")
    public String orderProductCouponPopup(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                          @RequestParam(value = "isDuplicable") Boolean dupl,
                                          Model model, @PathVariable String productNo,
                                          MemberInfo memberInfo) {
        couponRestApiModelSettingService.setProductCouponListToModelByPageAndMemberNoAndProductNo(
                pageNum, productNo, memberInfo, dupl, model);
        model.addAttribute("targetId", productNo+"coupon");
        model.addAttribute("productNo", productNo);
        model.addAttribute("isDuplicable", dupl);
        return RESOURCE_BASE + "couponPopupForOrder";
    }

    @GetMapping("/member")
    public String memberPopup(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                              Model model) {
        memberRestApiModelSettingService.setMemberListToModelByPage(pageNum, model);
        return RESOURCE_BASE + "memberPopup";
    }

    @GetMapping("/category")
    public String categoryPopup(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                Model model) {
        categoryRestApiModelSettingService.setCategoryListToModelByPage(pageNum, model);
        return RESOURCE_BASE + "categoryPopup";
    }

    @GetMapping("/product")
    public String productPopup(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                               Model model) {
        productRestApiModelSettingService.setProductListToModelByPage(pageNum, model);
        return RESOURCE_BASE + "productPopup";
    }
}
