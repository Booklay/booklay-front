package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponUsing;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.COUPON_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;

@RestController
@RequiredArgsConstructor
@RequestMapping("rest/coupons")
public class CouponRestController {
    private final RestService restService;
    private final String gatewayIp;
    @GetMapping("code")
    public ResponseEntity<CouponUsing> retrieveCouponByCouponCode(
            @RequestParam(value = "couponCode") String couponCode){
        String url = ControllerUtil.buildString(gatewayIp, DOMAIN_PREFIX_COUPON, COUPON_REST_PREFIX, "code");
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("couponCode", couponCode);
        ApiEntity<CouponUsing> apiEntity = restService.get(url, map, CouponUsing.class);
        return ResponseEntity.ok(apiEntity.getBody());
    }
}
