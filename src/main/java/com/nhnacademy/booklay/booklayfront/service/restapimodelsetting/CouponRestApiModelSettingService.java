package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

import com.nhnacademy.booklay.booklayfront.dto.coupon.*;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponHistoryResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class CouponRestApiModelSettingService {
    private final RestService restService;
    private final String gatewayIp;

    /**
     * 생성된 쿠폰 조회 요청을 보냅니다.
     *
     */
    public void setCouponListToModelByPage(Integer pageNum, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX);

        ApiEntity<PageResponse<Coupon>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});

        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    /**
     * 관리자의 멤버의 보유쿠폰 조회요청을 보내고 model에 결과값을 추가함?
     * @param memberNo 조회하려는 멤버의 no
     */
    public void setCouponListToModelByPageAndMemberNo(Integer pageNum, String memberNo, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "members/", memberNo);
        ApiEntity<PageResponse<Coupon>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    /**
     * 쿠폰의 상세 정보를 요청합니다.
     * @param couponId 상세 조회 하려는 쿠폰의 id
     */
    public void setCouponDetailToModelByCouponId(String couponId, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);
        apiEntity.getBody().setId(couponId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
    }
    public void setAllCouponTypeToModel(Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TYPES_REST_PREFIX);
        ApiEntity<PageResponse<CouponType>> apiEntity = restService.get(url, getDefaultPageMap(0, Integer.MAX_VALUE), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }
    public void setCouponTemplateListToModelByPage(Integer pageNum, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TEMPLATE_REST_PREFIX, COUPON_URL_LIST_PAGE);
        ApiEntity<PageResponse<CouponTemplate>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TEMPLATE_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }
    public void setCouponTemplateDetailToModelByCouponTemplateId(String couponTemplateId, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TEMPLATE_REST_PREFIX, couponTemplateId);
        ApiEntity<CouponTemplateDetail> apiEntity = restService.get(url, null, CouponTemplateDetail.class);
        apiEntity.getBody().setId(couponTemplateId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TEMPLATE_DETAIL, apiEntity.getBody());
    }
    public void setCouponSettingListToModelByPage(Integer pageNum, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_SETTING_REST_PREFIX, COUPON_URL_LIST_PAGE);
        ApiEntity<PageResponse<CouponSetting>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_SETTING_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    public void setCouponSettingToModelByCouponSettingId(String couponSettingId, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_SETTING_REST_PREFIX, couponSettingId);
        ApiEntity<CouponSetting> apiEntity = restService.get(url, null, CouponSetting.class);
        apiEntity.getBody().setId(couponSettingId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_SETTING, apiEntity.getBody());
    }



    public void setCouponUseHistoryToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "history");
        ApiEntity<PageResponse<CouponHistoryResponse>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_HISTORY_LIST,apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    /**
     * 발급된 쿠폰 리스트 조회 요청을 보냅니다.
     *
     */
    public void setCouponIssueToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "issue-history");

        ApiEntity<PageResponse<CouponHistoryResponse>> apiEntity =
                restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});

        model.addAttribute("list", apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    public void setCouponIssueToModelByPageAndMemberNo(Integer pageNum, String memberNo, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "issue-history/", memberNo);

        ApiEntity<PageResponse<CouponHistoryResponse>> apiEntity =
                restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});

        model.addAttribute("list", apiEntity.getBody().getData());
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    public void setCouponHistoryToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "history/");
        ApiEntity<PageResponse<CouponHistory>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    public void setCouponHistoryToModelByPageAndMemberNo(Integer pageNum, String memberNo, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "history/", memberNo);
        ApiEntity<PageResponse<CouponHistory>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }
}
