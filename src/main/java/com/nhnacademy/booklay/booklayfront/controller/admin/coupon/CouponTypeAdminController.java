package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_TYPES_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.PAGE_NUM;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.RETURN_ADMIN_PAGE;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponTypeAddRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons/types")
public class CouponTypeAdminController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    private final CouponRestApiModelSettingService restApiService;

    private static final String COUPON_TYPE_REDIRECT_LIST = "redirect:/admin/coupons/types/list/0";


    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    @GetMapping("/create")
    public String createCouponTypeForm(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/type/createCouponTypeForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("/create")
    public String postCreateCoupon(@Valid @ModelAttribute("CouponTypeAddRequest")
                                   CouponTypeAddRequest couponTypeAddRequest) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TYPES_REST_PREFIX);

        Map<String, Object> map = objectMapper.convertValue(couponTypeAddRequest, Map.class);
        restService.post(url, map, String.class);
        return COUPON_TYPE_REDIRECT_LIST;
    }

    @GetMapping("/list/{pageNum}")
    public String allCouponTypeList(Model model, @PathVariable Integer pageNum) {
        restApiService.setAllCouponTypeToModel(model);
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/type/typeListView");
        return RETURN_ADMIN_PAGE;
    }
    //todo type update 없음

    @GetMapping("/delete/{couponId}")
    public String couponTypeDelete(@PathVariable String couponId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TYPES_REST_PREFIX, couponId);
        restService.delete(url);
        return COUPON_TYPE_REDIRECT_LIST;
    }


}
