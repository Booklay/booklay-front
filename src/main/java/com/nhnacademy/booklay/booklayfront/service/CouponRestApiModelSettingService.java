package com.nhnacademy.booklay.booklayfront.service;

import com.nhnacademy.booklay.booklayfront.dto.coupon.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

@Service
@RequiredArgsConstructor
public class CouponRestApiModelSettingService {
    private final RestService restService;
    private final String gatewayIp;

    public void setAllCouponTypeToModel(Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX, COUPON_TYPES_REST_PREFIX);
        ApiEntity<PageResponse<CouponType>> apiEntity =
                restService.get(url, getDefaultPageMap(0, Integer.MAX_VALUE), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
    }
    public void setCouponTemplateListToModelByPage(Integer pageNum, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX, COUPON_TEMPLATE_REST_PREFIX, COUPON_URL_LIST_PAGE);
        ApiEntity<PageResponse<CouponTemplate>> apiEntity =
                restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TEMPLATE_LIST, apiEntity.getBody().getData());
    }
    public void setCouponTemplateDetailToModelByCouponTemplateId(String couponTemplateId, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX, COUPON_TEMPLATE_REST_PREFIX, couponTemplateId);
        ApiEntity<CouponTemplateDetail> apiEntity = restService.get(url, null, CouponTemplateDetail.class);
        apiEntity.getBody().setId(couponTemplateId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TEMPLATE_DETAIL, apiEntity.getBody());
    }
    public void setCouponSettingListToModelByPage(Integer pageNum, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX, COUPON_SETTING_REST_PREFIX, COUPON_URL_LIST_PAGE);
        ApiEntity<PageResponse<CouponSetting>> apiEntity =
                restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_SETTING_LIST, apiEntity.getBody().getData());
    }
    public void setCouponSettingToModelByCouponSettingId(String couponSettingId, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX, COUPON_SETTING_REST_PREFIX, couponSettingId);
        ApiEntity<CouponSetting> apiEntity = restService.get(url, null, CouponSetting.class);
        apiEntity.getBody().setId(couponSettingId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_SETTING, apiEntity.getBody());
    }
}
