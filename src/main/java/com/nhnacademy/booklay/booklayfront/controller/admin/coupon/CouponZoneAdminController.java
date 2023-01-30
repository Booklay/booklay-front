package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponZoneCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponZoneRetrieveResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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

    @GetMapping
    public String getCouponZoneList(@RequestParam(value = "page", defaultValue = "0") int page,
                                    Model model) {
        URI getLimitedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/limited");
        URI getUnlimitedUri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone/unlimited");

        ApiEntity<CouponZoneRetrieveResponse> limitedList =
            restService.get(getLimitedUri.toString(), null, CouponZoneRetrieveResponse.class);

        ApiEntity<CouponZoneRetrieveResponse> unlimitedList =
            restService.get(getUnlimitedUri.toString(), null, CouponZoneRetrieveResponse.class);

        model.addAttribute(TARGET_VIEW, "/coupon/couponZone/couponZoneList");
        model.addAttribute("limitedList", limitedList.getBody());
        model.addAttribute("unlimitedList", unlimitedList.getBody());

        return RETURN_PAGE;
    }

    /**
     * 쿠폰존에 쿠폰을 등록하기 위한 폼 호출.
     */
    @GetMapping("/form")
    public String getCouponZoneAddForm(Model model) {
        model.addAttribute(TARGET_VIEW, "/coupon/couponZone/couponZoneAddForm");

        return RETURN_PAGE;
    }

    @PostMapping
    public String createAtCouponZone(@Valid @ModelAttribute CouponZoneCreateRequest createRequest,
                                     @RequestParam(required = false, defaultValue = "false") Boolean isBlind) {

        createRequest.setIsBlind(isBlind);

        URI uri = URI.create(gatewayIp + COUPON_DOMAIN_PREFIX + "/admin/coupon-zone");
        restService.post(uri.toString(), objectMapper.convertValue(createRequest, Map.class), String.class);

        return "redirect:/admin/coupon-zone";
    }

}
