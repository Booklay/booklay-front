package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponTemplateAddRequest;
import com.nhnacademy.booklay.booklayfront.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon/template")
@SuppressWarnings("unchecked")
public class CouponTemplateAdminFrontController {
    private final RestService restService;
    private final ImageUploader imageUploader;
    private final CouponRestApiModelSettingService restApiService;
    private final String gatewayIp;
    private final ObjectMapper objectMapper;
    private static final String RETURN_PAGE_COUPON_TEMPLATE_LIST = "redirect:/admin/coupon/template/list/0";
    private static final String COUPON_TEMPLATE_HTML_PATH = "coupon/template/";

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponTemplateNavHead";
    }

    @GetMapping("create")
    public String createCouponTemplateForm(Model model){
        restApiService.setAllCouponTypeToModel(model);
        model.addAttribute(TARGET_VIEW, COUPON_TEMPLATE_HTML_PATH+"createCouponTemplateForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("create")
    public String createCouponTemplatePost(
        @Valid @ModelAttribute("CouponTemplateAddRequest") CouponTemplateAddRequest couponTemplateAddRequest,
        @RequestParam(name = "couponImage", required = false) MultipartFile image) throws IOException {
        Map<String, Object> map = objectMapper.convertValue(couponTemplateAddRequest, Map.class);
        imageUploader.uploadImage(image, map);
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON,
            ADMIN_COUPON_TEMPLATE_REST_PREFIX);
        restService.post(url, map, String.class);
        return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }

    @GetMapping("list")
    public String allCouponTemplateList(Model model, @RequestParam(value = "page", defaultValue = "0") Integer pageNum) {
        restApiService.setCouponTemplateListToModelByPage(pageNum, model);
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, COUPON_TEMPLATE_HTML_PATH+"templateListView");
        return RETURN_ADMIN_PAGE;
    }

    @GetMapping("detail/{couponTemplateId}")
    public String viewCouponDetail(Model model, @PathVariable String couponTemplateId) {
        restApiService.setCouponTemplateDetailToModelByCouponTemplateId(couponTemplateId, model);
        model.addAttribute(TARGET_VIEW, COUPON_TEMPLATE_HTML_PATH+"templateDetailView");
        return RETURN_ADMIN_PAGE;
    }

    @GetMapping("update/{couponTemplateId}")
    public String updateCouponForm(Model model, @PathVariable String couponTemplateId) {
        restApiService.setAllCouponTypeToModel(model);
        restApiService.setCouponTemplateDetailToModelByCouponTemplateId(couponTemplateId, model);
        model.addAttribute(TARGET_VIEW, COUPON_TEMPLATE_HTML_PATH+"couponTemplateUpdateForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("update/{couponTemplateId}")
    public String postUpdateCouponForm(@Valid @ModelAttribute CouponTemplateAddRequest couponTemplateAddRequest,
                                       @PathVariable String couponTemplateId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, couponTemplateId);
        Map<String, Object> map = objectMapper.convertValue(couponTemplateAddRequest, Map.class);
        restService.put(url, map, String.class);

        return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }

    @GetMapping("delete/{couponTemplateId}")
    public String deleteCouponTemplate(@PathVariable String couponTemplateId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TEMPLATE_REST_PREFIX, couponTemplateId);
        restService.delete(url);
        return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }
}
