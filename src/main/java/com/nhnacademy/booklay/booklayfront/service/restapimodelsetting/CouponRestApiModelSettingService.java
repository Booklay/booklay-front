package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_SETTING_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_TEMPLATE_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_TYPES_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_COUPON_DETAIL;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_COUPON_LIST;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_COUPON_SETTING;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_COUPON_SETTING_LIST;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_COUPON_TEMPLATE_DETAIL;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_COUPON_TEMPLATE_LIST;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_COUPON_TYPE_LIST;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_HISTORY_LIST;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_ISSUE_HISTORY_LIST;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_ISSUE_LIST;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.COUPON_URL_LIST_PAGE;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;

import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponDetail;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponHistory;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponIssue;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponSetting;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponTemplate;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponTemplateDetail;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponType;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponHistoryResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
@RequiredArgsConstructor
public class CouponRestApiModelSettingService {
    private final RestService restService;
    private final String gatewayIp;

    public void setCouponListToModelByPage(Integer pageNum, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "pages");
        ApiEntity<PageResponse<Coupon>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody().getData());
    }

    public void setCouponListToModelByPageAndMemberNo(Integer pageNum, String memberNo, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, "/members/", memberNo, "/", pageNum.toString());
        ApiEntity<PageResponse<Coupon>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody());
    }

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
    }
    public void setCouponTemplateListToModelByPage(Integer pageNum, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TEMPLATE_REST_PREFIX, COUPON_URL_LIST_PAGE);
        ApiEntity<PageResponse<CouponTemplate>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TEMPLATE_LIST, apiEntity.getBody().getData());
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
    }

    public void setCouponSettingToModelByCouponSettingId(String couponSettingId, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_SETTING_REST_PREFIX, couponSettingId);
        ApiEntity<CouponSetting> apiEntity = restService.get(url, null, CouponSetting.class);
        apiEntity.getBody().setId(couponSettingId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_SETTING, apiEntity.getBody());
    }



    public void setCouponIssueHistoryToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "issue-history");
        ApiEntity<List<CouponHistoryResponse>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_HISTORY_LIST,apiEntity.getBody());
    }

    public void setCouponIssueToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, "/issue/", pageNum.toString());
        ApiEntity<PageResponse<CouponHistory>> apiEntity =
                restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody().getData());
    }

    public void setCouponIssueToModelByPageAndMemberNo(Integer pageNum, String memberNo, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "issue/", memberNo, "/", pageNum.toString());
        ApiEntity<PageResponse<CouponHistory>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody().getData());
    }

    public void setCouponHistoryToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "history/");
        ApiEntity<PageResponse<CouponHistory>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody().getData());
    }

    public void setCouponHistoryToModelByPageAndMemberNo(Integer pageNum, String memberNo, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "history/", memberNo);
        ApiEntity<PageResponse<CouponHistory>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody().getData());
    }
}
