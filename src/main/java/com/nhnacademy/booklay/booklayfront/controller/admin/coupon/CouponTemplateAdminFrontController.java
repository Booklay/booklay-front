package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.*;
import com.nhnacademy.booklay.booklayfront.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
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
    private final String gatewayIp;
    private final ImageUploader imageUploader;
    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String RETURN_PAGE_COUPON_TEMPLATE_LIST = "redirect:/admin/coupon/template/list/0";
    private static final String REST_PREFIX = "/coupon/v1";
    private static final String URL_PREFIX = "/admin/couponTemplates/";
    private final ObjectMapper objectMapper;

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    @GetMapping("create")
    public String createCouponTemplateForm(Model model){
        String url = buildString(gatewayIp, REST_PREFIX, "/admin/couponTypes");
        ApiEntity<PageResponse<CouponType>>
                apiEntity = restService.get(url, getDefaultPageMap(0),
                new ParameterizedTypeReference<>() {
                });

        if (!apiEntity.isSuccess()) {
            return ERROR;
        }

        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
        model.addAttribute(TARGET_VIEW, "coupon/createCouponTemplateForm");
        return RETURN_PAGE;
    }

    @PostMapping("create")
    public String createCouponTemplatePost(
        @ModelAttribute("CouponTemplateAddRequest") CouponTemplateAddRequest couponTemplateAddRequest,
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
            String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX);
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
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/pages");
        ApiEntity<PageResponse<CouponTemplate>> apiEntity =
                restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
                });
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }

        // FIXME : Boolean is null.
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TEMPLATE_LIST, apiEntity.getBody().getData());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/templateListView");
        return RETURN_PAGE;
    }

    @GetMapping("detail/{couponTemplateId}")
    public String viewCouponDetail(Model model, @PathVariable String couponTemplateId) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/", couponTemplateId);
        ApiEntity<CouponTemplateDetail> apiEntity = restService.get(url, null, CouponTemplateDetail.class);
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        model.addAttribute(TARGET_VIEW, "coupon/templateDetailView");
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TEMPLATE_DETAIL, apiEntity.getBody());
        return RETURN_PAGE;
    }

    @GetMapping("update/{couponTemplateId}")
    public String updateCouponForm(Model model, @PathVariable String couponTemplateId) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/", couponTemplateId);
        ApiEntity<CouponTemplateDetail> apiEntity = restService.get(url, null, CouponTemplateDetail.class);
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        apiEntity.getBody().setId(couponTemplateId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TEMPLATE_DETAIL, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/couponTemplateUpdateForm");
        return RETURN_PAGE;
    }

    @PostMapping("update/{couponTemplateId}")
    public String postUpdateCouponForm(@ModelAttribute CouponTemplateAddRequest couponTemplateAddRequest,
                                       @PathVariable String couponTemplateId) {
        String url = buildString(gatewayIp, REST_PREFIX, couponTemplateId);
        Map<String, Object> map = objectMapper.convertValue(couponTemplateAddRequest, Map.class);
        ApiEntity<String> apiEntity = restService.put(url, map, String.class);
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }

    @GetMapping("delete/{couponTemplateId}")
    public String deleteCouponTemplate(@PathVariable String couponTemplateId) {
        String url = buildString(gatewayIp, REST_PREFIX, URL_PREFIX, "/", couponTemplateId);
        restService.delete(url);
        return RETURN_PAGE_COUPON_TEMPLATE_LIST;
    }


}
