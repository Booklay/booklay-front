package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_MEMBER_NO;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.PAGE_NUM;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.RETURN_ADMIN_PAGE;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponAddRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
@Slf4j
public class CouponAdminFrontController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    private final CouponRestApiModelSettingService couponRestApiModelSettingService;

    private static final String RETURN_PAGE_COUPON_LIST = "redirect:/admin/coupons/list/0";
    

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    @GetMapping
    public String adminCouponPage(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/empty");
        return RETURN_ADMIN_PAGE;
    }

    @GetMapping("/create")
    public String createCouponForm(Model model) {
        couponRestApiModelSettingService.setAllCouponTypeToModel(model);
        model.addAttribute(TARGET_VIEW, "coupon/createCouponForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("/create")
    public String postCreateCoupon(
        @Valid @ModelAttribute("CouponTypeAddRequest") CouponAddRequest couponAddRequest,
        @RequestParam("issuanceDeadline")
        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date date,
        @RequestParam(name = "couponImage", required = false) MultipartFile multipartFile) {
        couponAddRequest.setIssuanceDeadlineAt(
            date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        Map<String, Object> map = objectMapper.convertValue(couponAddRequest, Map.class);
        map.put("couponImage", multipartFile);
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX);

        restService.post(url, map, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }


    @GetMapping("/list")
    public String allCouponList0() {
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("/list/{pageNum}")
    public String allCouponList(Model model, @PathVariable Integer pageNum) {
        couponRestApiModelSettingService.setCouponListToModelByPage(pageNum, model);
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_ADMIN_PAGE;
    }


    @GetMapping("list/{memberNo}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberNo,
                                   @PathVariable Integer pageNum) {
        couponRestApiModelSettingService.setCouponListToModelByPageAndMemberNo(pageNum, memberNo, model);
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_ADMIN_PAGE;
    }


    @GetMapping("/detail/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId) {
        couponRestApiModelSettingService.setCouponDetailToModelByCouponId(couponId, model);
        model.addAttribute(TARGET_VIEW, "coupon/detailView");
        return RETURN_ADMIN_PAGE;
    }

    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId) {
        couponRestApiModelSettingService.setCouponDetailToModelByCouponId(couponId, model);
        model.addAttribute(TARGET_VIEW, "coupon/couponUpdateForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("update/{couponId}")
    public String postUpdateCouponForm(@Valid @ModelAttribute CouponAddRequest couponAddRequest,
                                       @PathVariable String couponId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, couponId);
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponAddRequest);
        restService.put(url, map, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, couponId);
        restService.delete(url);
        return RETURN_PAGE_COUPON_LIST;
    }


}
