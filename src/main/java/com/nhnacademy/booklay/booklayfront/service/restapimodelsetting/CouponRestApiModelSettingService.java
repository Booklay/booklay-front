package com.nhnacademy.booklay.booklayfront.service.restapimodelsetting;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.*;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponHistoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponUsedHistoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.MemberOwnedCouponResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.type.CouponType;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;

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
     * 해당 상품에 적용가능한 쿠폰 리스트를 모델에 추가함
     *
     * @param pageNum   페이지번호
     * @param productNo 상품번호
     * @param dupl      중복쿠폰여부
     */
    public void setProductCouponListToModelByPageAndMemberNoAndProductNo(int pageNum, String productNo, MemberInfo memberInfo, Boolean dupl, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "product/", productNo);
        setCouponListToModelForPopUpPage(url, pageNum, dupl, memberInfo, model);
    }
    /**
     * 사용자의 주문 쿠폰 리스트를 모델에 추가함
     * @param pageNum       페이지번호
     * @param dupl          중복쿠폰여부
     */
    public void setOrderCouponListToModelByPageAndMemberNo(int pageNum, Boolean dupl, MemberInfo memberInfo, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "order");
        setCouponListToModelForPopUpPage(url, pageNum, dupl, memberInfo, model);
    }
    /**
     * 팝업창 쿠폰리스트 조회 중복코드 처리
     */
    private void setCouponListToModelForPopUpPage(String url, int pageNum, Boolean dupl, MemberInfo memberInfo, Model model){
        MultiValueMap<String, String> map = getDefaultPageMap(pageNum, memberInfo);
        map.add("isDuplicable", dupl.toString());
        ApiEntity<PageResponse<CouponUsing>> apiEntity = restService.get(url, map, new ParameterizedTypeReference<>() {});
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

        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
    }

    /**
     * Coupon Type을 model에 추가.
     */
    public void setAllCouponTypeToModel(Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TYPES_REST_PREFIX);
        ApiEntity<PageResponse<CouponType>> apiEntity = restService.get(url, getDefaultPageMap(0, Integer.MAX_VALUE), new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
    }
    public void setCouponTypeListToModelByPage(Integer pageNum, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_TYPES_REST_PREFIX);
        ApiEntity<PageResponse<CouponType>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
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

    /**
     * 관리자의 쿠폰 사용 내역을 조회합니다.
     */
    public void setCouponHistoryToModelByPage(Integer pageNum, Model model) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "history");
        ApiEntity<PageResponse<CouponUsedHistoryResponse>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});

        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody().getData());
        setCurrentPageAndMaxPageToModel(model, apiEntity.getBody());
    }

    /**
     * 사용자가 소유한 쿠폰 목록을 조회합니다.
     */
    public void setOwnedCouponToModelByMember(Integer pageNum, Long memberNo, Model model) {
        String url = ControllerUtil.buildString(gatewayIp, DOMAIN_PREFIX_COUPON, "/members/" + memberNo + "/coupons");

        ApiEntity<PageResponse<MemberOwnedCouponResponse>> apiEntity =
            restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {
            });

        List<MemberOwnedCouponResponse> data = apiEntity.getBody().getData();
        List<MemberOwnedCouponResponse> unused = data.stream().filter(c -> !c.getIsUsed()).collect(
            Collectors.toList());
        List<MemberOwnedCouponResponse> used = data.stream().filter(c -> c.getIsUsed()).collect(
            Collectors.toList());

        model.addAttribute("unusedList", unused);
        model.addAttribute("usedList", used);

    }
}
