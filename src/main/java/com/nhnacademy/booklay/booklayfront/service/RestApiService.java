package com.nhnacademy.booklay.booklayfront.service;

import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponType;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_COUPON_TYPE_LIST;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;

@Service
@RequiredArgsConstructor
public class RestApiService {
    private final RestService restService;
    private final String gatewayIp;
    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String RETURN_PAGE_COUPON_LIST = "redirect:/admin/coupon/list/0";
    private static final String REST_PREFIX = "/coupon/v1";
    private static final String URL_PREFIX = "/admin/coupons";
    public void setAllCouponTypeToModel(Model model){
        String url = buildString(gatewayIp, REST_PREFIX, "/admin/couponTypes");
        ApiEntity<PageResponse<CouponType>> apiEntity =
                restService.get(url, getDefaultPageMap(0), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
    }
}
