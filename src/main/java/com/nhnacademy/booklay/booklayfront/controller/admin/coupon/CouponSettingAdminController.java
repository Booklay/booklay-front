package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponSettingAddRequest;
import com.nhnacademy.booklay.booklayfront.service.CouponRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon/setting")
public class CouponSettingAdminController {
    private final RestService restService;
    private final CouponRestApiModelSettingService restApiService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;
    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String RETURN_PAGE_COUPON_SETTING_LIST = "redirect:/admin/coupon/setting/list/0";

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponTemplateNavHead";
    }

    @GetMapping("create")
    public String createCouponSettingForm(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/setting/createCouponSettingForm");
        return RETURN_PAGE;
    }

    @PostMapping("create")
    public String postCreateCouponSetting(
            @ModelAttribute("CouponSettingAddRequest") CouponSettingAddRequest couponSettingAddRequest) {
        Map<String, Object> map = objectMapper.convertValue(couponSettingAddRequest, Map.class);
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_SETTING_URL_PREFIX);
        restService.post(url, map, String.class);
        return RETURN_PAGE_COUPON_SETTING_LIST;
    }

    @GetMapping("list")
    public String allCouponSettingList0() {
        return RETURN_PAGE_COUPON_SETTING_LIST;
    }

    @GetMapping("list/{pageNum}")
    public String allCouponSettingList(Model model, @PathVariable Integer pageNum) {
        restApiService.setCouponSettingListToModelByPage(pageNum, model);
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/setting/settingListView");
        return RETURN_PAGE;
    }

    @GetMapping("update/{couponSettingId}")
    public String updateCouponSettingForm(Model model, @PathVariable String couponSettingId) {
        restApiService.setCouponSettingToModelByCouponSettingId(couponSettingId, model);
        model.addAttribute(TARGET_VIEW, "coupon/setting/updateCouponSettingForm");
        return RETURN_PAGE;
    }

    @PostMapping("update/{couponSettingId}")
    public String postUpdateCouponSetting(
            @ModelAttribute("CouponSettingAddRequest") CouponSettingAddRequest couponSettingAddRequest
            , @PathVariable String couponSettingId) {
        Map<String, Object> map = objectMapper.convertValue(couponSettingAddRequest, Map.class);
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_SETTING_URL_PREFIX, couponSettingId);
        restService.put(url, map, String.class);
        return RETURN_PAGE_COUPON_SETTING_LIST;
    }

    @GetMapping("delete/{couponSettingId}")
    public String deleteCoupon(@PathVariable String couponSettingId) {
        String url = buildString(gatewayIp, REST_PREFIX, COUPON_SETTING_URL_PREFIX, couponSettingId);
        restService.delete(url);
        return RETURN_PAGE_COUPON_SETTING_LIST;
    }

}
