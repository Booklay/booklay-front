package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponZoneCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponZoneIsBlindRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponZoneIsBlindResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponZoneRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.dto.grade.Grade;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon-zone")
@Slf4j
public class CouponZoneAdminController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    private final String COUPON_ZONE_RESOURCE_BASE = "admin/coupon/couponZone/";
    private final String COUPON_DOMAIN_PREFIX = "/coupon/v1";

    /**
     * 관리자의 쿠폰존 조회.
     * 이달의 쿠폰(수량 제한 쿠폰), 등급별 쿠폰, 무제한 쿠폰을 불러옵니다.
     */
    @GetMapping
    public String getCouponZoneList(@RequestParam(value = "page", defaultValue = "0") int page,
                                    Model model) {
        String query = "?page=" + page;

        // TODO Coupon으로 가는거 말고, Shop으로 가게끔.
        URI getLimitedUri =
            URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/limited" + query);
        URI getGradedUri =
            URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/graded" + query);
        URI getUnlimitedUri =
            URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/unlimited" + query);

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> limitedList =
            restService.get(getLimitedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> gradedList =
            restService.get(getGradedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        ApiEntity<PageResponse<CouponZoneRetrieveResponse>> unlimitedList =
            restService.get(getUnlimitedUri.toString(), null, new ParameterizedTypeReference<>() {
            });

        model.addAttribute("grades", Grade.values());
        model.addAttribute("limitedList", limitedList.getBody().getData());
        model.addAttribute("gradedList", gradedList.getBody().getData());
        model.addAttribute("unlimitedList", unlimitedList.getBody().getData());

        return COUPON_ZONE_RESOURCE_BASE + "couponZoneList";
    }

    /**
     * 쿠폰존에 쿠폰을 등록하기 위한 폼 호출.
     */
    @GetMapping("/create-form")
    public String getCouponZoneAddForm(Model model) {
        model.addAttribute("grades", Grade.values());

        return COUPON_ZONE_RESOURCE_BASE + "couponZoneAddForm";
    }

    /**
     * 관리자가 쿠폰을 쿠폰존에 등록합니다.
     * 쿠폰존에 등록된 쿠폰 중, isBlind = false 인 쿠폰만 사용자가 조회 가능합니다.
     */
    @PostMapping("/create-form")
    public String createAtCouponZone(@Valid @ModelAttribute CouponZoneCreateRequest createRequest) {
        URI uri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone");
        Map map = objectMapper.convertValue(createRequest, Map.class);

        if(createRequest.getGrade().equals("0")) {
            map.replace("grade", null);
        }

        restService.post(uri.toString(), map, String.class);

        return "redirect:/admin/coupon-zone";
    }

    /**
     * 숨김 여부 수정 폼.
     */
    @GetMapping("/update-form/{couponZoneId}")
    public String getCouponZoneUpdateForm(Model model, @PathVariable Long couponZoneId) {
        URI detailUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/blind/" + couponZoneId);

        ApiEntity<CouponZoneIsBlindResponse> isBlind = restService.get(detailUri.toString(), null, CouponZoneIsBlindResponse.class);

        model.addAttribute("isBlind", isBlind.getBody().getIsBlind());

        return "admin/coupon/couponZone/couponZoneUpdateForm";
    }

    /**
     * 숨김 여부 수정.
     */
    @PostMapping("/update-form/{couponZoneId}")
    public String updateCouponZone(@Valid @ModelAttribute CouponZoneIsBlindRequest request,
                                   @PathVariable Long couponZoneId) {
        Map map = objectMapper.convertValue(request, Map.class);
        URI updateUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/blind/" + couponZoneId);

        restService.post(updateUri.toString(), map, String.class);

        return "redirect:/admin/coupon-zone";
    }

    @GetMapping("/delete/{couponZoneId}")
    public String deleteAtCouponZone(@PathVariable Long couponZoneId) {
        URI uri =
            URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/" + couponZoneId);

        restService.delete(uri.toString());

        return "redirect:/admin/coupon-zone";
    }
}
