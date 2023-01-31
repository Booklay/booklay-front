package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponZoneCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponZoneRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon-zone")
public class CouponZoneAdminController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    private static final String COUPON_DOMAIN_PREFIX = "/coupon/v1";
    private static final String RETURN_PAGE = "admin/adminPage";

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    /**
     * 관리자의 쿠폰존 조회.
     * 수량 제한 있는 쿠폰과, 제한 없는 쿠폰을 각 각 받아와서 보여줍니다.
     *
     */
    @GetMapping
    public String getCouponZoneList(@RequestParam(value = "page", defaultValue = "0") int page,
                                    Model model) {
        String query = "?page=" + page;

        URI getLimitedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/limited" + query);
        URI getUnlimitedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/unlimited" + query);

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> limitedList =
            restService.get(getLimitedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> unlimitedList =
            restService.get(getUnlimitedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        model.addAttribute(TARGET_VIEW, "coupon/couponZone/couponZoneList");
        model.addAttribute("limitedList", limitedList.getBody().getData());
        model.addAttribute("unlimitedList", unlimitedList.getBody().getData());

        return RETURN_PAGE;
    }

    /**
     * 쿠폰존에 쿠폰을 등록하기 위한 폼 호출.
     *
     */
    @GetMapping("/form")
    public String getCouponZoneAddForm(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/couponZone/couponZoneAddForm");

        return RETURN_PAGE;
    }

    /**
     * 관리자가 쿠폰을 쿠폰존에 등록합니다.
     * 쿠폰존에 등록된 쿠폰 중, isBlind = false 인 쿠폰만 사용자가 조회 가능합니다.
     *
     */
    @PostMapping
    public String createAtCouponZone(@Valid @ModelAttribute CouponZoneCreateRequest createRequest,
                                     @RequestParam(required = false, defaultValue = "false") Boolean isBlind) {
        createRequest.setIsBlind(isBlind);

        URI uri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone");
        restService.post(uri.toString(), objectMapper.convertValue(createRequest, Map.class), String.class);

        return "redirect:/admin/coupon-zone";
    }

}
