package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.nhnacademy.booklay.booklayfront.coupon.domain.*;
import com.nhnacademy.booklay.booklayfront.coupon.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nhnacademy.booklay.booklayfront.coupon.domain.ControllerStrings.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/coupon")
public class CouponAdminFrontController {
    private final RestService restService;
    private final ImageUploader imageUploader;
    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String REST_PRE_FIX = "admin/coupon/";
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
    public String createCouponTypeForm(Model model){
        model.addAttribute(TARGET_VIEW, "coupon/createCouponTypeForm");
        return RETURN_PAGE;
    }

    @PostMapping("create")
    public String postCreateCouponType(@ModelAttribute("CouponTypeAddRequest") CouponTypeAddRequest couponTypeAddRequest
                        ,@RequestParam("issuanceDeadline") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date date
                        ,@RequestParam(name = "couponImage", required = false) MultipartFile multipartFile
                        ,HttpServletRequest request){
        couponTypeAddRequest.setIssuanceDeadlineAt(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        imageUploader.uploadImage(multipartFile, request);
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponTypeAddRequest);
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, "add");
        ApiEntity<String> apiEntity = restService.post(url, map, String.class);
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        return "redirect:";
    }
    @GetMapping("list")
    public String allCouponList0(){
        return "redirect:list/0";
    }
    @GetMapping("list/{pageNum}")
    public String allCouponList(Model model, @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, pageNum.toString());
        ApiEntity<List<Coupon>> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }

    @GetMapping("list/type/{pageNum}")
    public String allCouponTypeList(Model model, @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, "type/", pageNum.toString());
        ApiEntity<List<CouponType>> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody());
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/typeListView");
        return RETURN_PAGE;
    }

    @GetMapping("list/{memberNo}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberNo,
                                   @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, memberNo , "/" , pageNum.toString());
        ApiEntity<List<Coupon>> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>(){});
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
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX,"detail",couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/detailView");
        return RETURN_PAGE;
    }

    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId){
        ApiEntity<CouponDetail> apiEntity = restService.get(FrontURI.SHOP_URI + REST_PRE_FIX + "update"+couponId, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        apiEntity.getBody().setCouponId(couponId);
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/couponUpdateForm");
        return RETURN_PAGE;
    }

    @PostMapping("update/{couponId}")
    public String postUpdateCouponForm(@ModelAttribute CouponTypeAddRequest couponTypeAddRequest,
                                       @PathVariable String couponId){
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponTypeAddRequest);
        ApiEntity<String> apiEntity = restService.put(FrontURI.SHOP_URI + REST_PRE_FIX+couponId, map, String.class);
        if (!apiEntity.isSuccess()){
            return ERROR;
        }
        return "redirect:";
    }

    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId){

        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX , "delete/", couponId);
        restService.delete(url);
        return "redirect:";
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
}
