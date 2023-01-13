package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.coupon.domain.*;
import com.nhnacademy.booklay.booklayfront.coupon.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.nhnacademy.booklay.booklayfront.coupon.domain.ControllerStrings.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/coupon")
public class CouponAdminFrontController {
    private final RestService restService;
    private final String gatewayIp;
    private final ImageUploader imageUploader;
    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String RETURN_PAGE_COUPON_LIST = "redirect:/admin/coupon/list/0";
    private static final String REST_PRE_FIX = "/shop/v1/admin/coupons/";
    @ModelAttribute("navHead")
    public String addNavHead(){
        return "coupon/couponFragments/couponNavHead";
    }
    @GetMapping("")
    public String adminCouponPage(Model model){
        model.addAttribute(TARGET_VIEW, "coupon/empty");
        return RETURN_PAGE;
    }

    @GetMapping("create")
    public String createCouponForm(Model model){
        String url = buildString(gatewayIp, "/shop/v1/couponTypes");
        ApiEntity<PageResponse<CouponType>> apiEntity = restService.get(url, getDefaultPageMap(0, Integer.MAX_VALUE), new ParameterizedTypeReference<>() {});
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
        model.addAttribute(TARGET_VIEW, "coupon/createCouponForm");
        return RETURN_PAGE;
    }

    @PostMapping("create")
    public String postCreateCoupon(@ModelAttribute("CouponTypeAddRequest") CouponAddRequest couponAddRequest
                        ,@RequestParam("issuanceDeadline") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date date
                        ,@RequestParam(name = "couponImage", required = false) MultipartFile multipartFile
                        ,HttpServletRequest request){
        couponAddRequest.setIssuanceDeadlineAt(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        String imagePath = imageUploader.uploadImage(multipartFile, request);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(couponAddRequest, Map.class);
        map.put("imagePath", imagePath);
        String url = buildString(gatewayIp, REST_PRE_FIX);
        ApiEntity<String> apiEntity = restService.post(url, map, String.class);
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("type/create")
    public String createCouponTypeForm(Model model){
        model.addAttribute(TARGET_VIEW, "coupon/createCouponTypeForm");
        return RETURN_PAGE;
    }

    @PostMapping("type/create")
    public String postCreateCoupon(@ModelAttribute("CouponTypeAddRequest") CouponTypeAddRequest couponTypeAddRequest){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(couponTypeAddRequest, Map.class);
        String url = buildString(gatewayIp, "/shop/v1/couponTypes");
        restService.post(url, map, String.class);
        return "redirect:/admin/coupon/list/type/0";
    }

    @GetMapping("list")
    public String allCouponList0(){
        return RETURN_PAGE_COUPON_LIST;
    }
    @GetMapping("list/{pageNum}")
    public String allCouponList(Model model, @PathVariable Integer pageNum){
        String url = buildString(gatewayIp, REST_PRE_FIX, "pages");
        ApiEntity<PageResponse<Coupon>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody().getData());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }

    @GetMapping("list/type/{pageNum}")
    public String allCouponTypeList(Model model, @PathVariable Integer pageNum){

        String url = buildString(gatewayIp, "/shop/v1/couponTypes");
        ApiEntity<PageResponse<CouponType>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>() {});
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_TYPE_LIST, apiEntity.getBody().getData());
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/typeListView");
        return RETURN_PAGE;
    }

    @GetMapping("type/delete/{couponId}")
    public String couponTypeDelete(@PathVariable String couponId){
        String url = buildString(gatewayIp, "/shop/v1/couponTypes/" , couponId);
        restService.delete(url);
        return "redirect:/admin/coupon/list/type/0";
    }

    @GetMapping("list/{memberNo}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberNo,
                                   @PathVariable Integer pageNum){
        //todo
        String url = buildString(gatewayIp, REST_PRE_FIX, memberNo , "/" , pageNum.toString());
        ApiEntity<PageResponse<Coupon>> apiEntity = restService.get(url, getDefaultPageMap(pageNum), new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo+"/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }


    @GetMapping("detail/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId){
        String url = buildString(gatewayIp, REST_PRE_FIX,couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        model.addAttribute(TARGET_VIEW, "coupon/detailView");
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        return RETURN_PAGE;
    }

    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId){
        String url = buildString(gatewayIp, REST_PRE_FIX, couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        apiEntity.getBody().setId(couponId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/couponUpdateForm");
        return RETURN_PAGE;
    }

    @PostMapping("update/{couponId}")
    public String postUpdateCouponForm(@ModelAttribute CouponAddRequest couponAddRequest,
                                       @PathVariable String couponId){
        String url = buildString(gatewayIp, REST_PRE_FIX, couponId);
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponAddRequest);
        ApiEntity<String> apiEntity = restService.put(url, map, String.class);
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId){
        String url = buildString(gatewayIp, REST_PRE_FIX , couponId);
        restService.delete(url);
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("history/{pageNum}")
    public String historyCoupon(Model model, @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX , "history/", pageNum.toString());
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/historyView");
        return RETURN_PAGE;

    }

    @GetMapping("history/{memberNo}/{pageNum}")
    public String memberHistoryCoupon(Model model, @PathVariable String memberNo,
                                @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX , "history/", memberNo, "/", pageNum.toString());
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo+"/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/historyView");
        return RETURN_PAGE;
    }

    @GetMapping("issue/{pageNum}")
    public String issueCoupon(Model model, @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, "issue/", pageNum.toString());
        ApiEntity<CouponIssue> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>(){});
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_PAGE;
    }

    @GetMapping("issue/{memberNo}/{pageNum}")
    public String memberIssueCoupon(Model model, @PathVariable Integer pageNum,
                              @PathVariable String memberNo){
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, "issue/", memberNo, "/", pageNum.toString());
        ApiEntity<CouponIssue> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo+"/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_PAGE;

    }


    private String buildString(String ... strings){
        StringBuilder builder = new StringBuilder();
        for (String s : strings){
            builder.append(s);
        }
        return builder.toString();
    }
    private MultiValueMap<String, String> getDefaultPageMap(Integer pageNum, Integer size) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("page", pageNum.toString());
        map.add("size", size.toString());
        return map;
    }
    private MultiValueMap<String, String> getDefaultPageMap(Integer pageNum) {
        return getDefaultPageMap(pageNum, 20);
    }
}
