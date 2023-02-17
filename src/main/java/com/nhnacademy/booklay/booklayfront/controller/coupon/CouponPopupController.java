package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("order")
public class CouponPopupController {
    private final CouponRestApiModelSettingService couponRestApiModelSettingService;
    private static final String RESOURCE_BASE = "order/popup/";

    @GetMapping("popup/coupon/order")
    public String orderOrderCouponPopup(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                        @RequestParam(value = "isDuplicable") Boolean dupl,
                                        Model model, MemberInfo memberInfo){
        couponRestApiModelSettingService.setOrderCouponListToModelByPageAndMemberNo(pageNum, dupl, memberInfo, model);
        model.addAttribute("targetId", "ordercoupon");
        model.addAttribute("productNo", "order");
        model.addAttribute("isDuplicable", dupl);
        return RESOURCE_BASE + "couponPopupForOrder";
    }
    @GetMapping("popup/coupon/{productNo}")
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

}
