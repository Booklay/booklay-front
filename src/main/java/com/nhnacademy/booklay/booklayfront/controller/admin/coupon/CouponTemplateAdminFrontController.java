package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.*;
import com.nhnacademy.booklay.booklayfront.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.service.CouponRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon/template")
public class CouponTemplateAdminFrontController {
    private final RestService restService;
    private final CouponRestApiModelSettingService restApiService;
    private final String gatewayIp;
    private final ImageUploader imageUploader;
    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String RETURN_PAGE_COUPON_TEMPLATE_LIST = "redirect:/admin/coupon/template/list/0";
    private final ObjectMapper objectMapper;

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponTemplateNavHead";
    }

    @GetMapping("create")
    public String createCouponTemplateForm(Model model){
        restApiService.setAllCouponTypeToModel(model);
        model.addAttribute(TARGET_VIEW, "coupon/createCouponTemplateForm");
        return RETURN_PAGE;
    }

    @PostMapping("create")
    public String createCouponTemplatePost(
        @Valid @ModelAttribute("CouponTemplateAddRequest") CouponTemplateAddRequest couponTemplateAddRequest,
        @RequestParam("issuingDeadLine")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date date,
        @RequestParam(name = "couponImage", required = false) MultipartFile multipartFile,
        HttpServletRequest request) {
            couponTemplateAddRequest.setIssuingDeadLine(
                    date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            String imagePath = imageUploader.uploadImage(multipartFile, request);
            Map<String, Object> map = objectMapper.convertValue(couponTemplateAddRequest, Map.class);

            // FIXME Image 저장 후, 반환 값
            map.put("imageId", 1L);
            String url = buildString(gatewayIp, DOMAIN_PREFIX, COUPON_TEMPLATE_REST_PREFIX);
            ApiEntity<String> apiEntity = restService.post(url, map, String.class);
            if (!apiEntity.isSuccess()) {
                return ERROR;
            }
            return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }

    @GetMapping("list")
    public String allCouponTemplateList0() {
        return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }

    @GetMapping("list/{pageNum}")
    public String allCouponTemplateList(Model model, @PathVariable Integer pageNum) {
        restApiService.setCouponTemplateListToModelByPage(pageNum, model);
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/templateListView");
        return RETURN_PAGE;
    }

    @GetMapping("detail/{couponTemplateId}")
    public String viewCouponDetail(Model model, @PathVariable String couponTemplateId) {
        restApiService.setCouponTemplateDetailToModelByCouponTemplateId(couponTemplateId, model);
        model.addAttribute(TARGET_VIEW, "coupon/templateDetailView");
        return RETURN_PAGE;
    }

    @GetMapping("update/{couponTemplateId}")
    public String updateCouponForm(Model model, @PathVariable String couponTemplateId) {
        restApiService.setAllCouponTypeToModel(model);
        restApiService.setCouponTemplateDetailToModelByCouponTemplateId(couponTemplateId, model);
        model.addAttribute(TARGET_VIEW, "coupon/couponTemplateUpdateForm");
        return RETURN_PAGE;
    }

    @PostMapping("update/{couponTemplateId}")
    public String postUpdateCouponForm(@Valid @ModelAttribute CouponTemplateAddRequest couponTemplateAddRequest,
                                       @PathVariable String couponTemplateId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX, couponTemplateId);
        Map<String, Object> map = objectMapper.convertValue(couponTemplateAddRequest, Map.class);
        restService.put(url, map, String.class);

        return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }

    @GetMapping("delete/{couponTemplateId}")
    public String deleteCouponTemplate(@PathVariable String couponTemplateId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX, COUPON_TEMPLATE_REST_PREFIX, couponTemplateId);
        restService.delete(url);
        return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }


}
