package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.nhnacademy.booklay.booklayfront.coupon.domain.*;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import java.lang.reflect.Member;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("admin/coupon")
public class CouponAdminFrontController {
    private final RestService restService;
    private static final String TARGET_VIEW = "targetUrl";
    private static final String RETURN_PAGE = "admin/adminPage";
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
    ,@RequestParam("issuanceDeadline") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") Date date){
        CouponTypeAddRequest couponTypeAddRequest2 = new CouponTypeAddRequest("coupon1", 1L, "정액", 1000L, 12L, 10000L, 1000L, true, 500L);
        couponTypeAddRequest2.setIssuanceDeadlineAt(new Date(130, 9, 30, 12, 34, 0)
                .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        Map<String, Object> map2 = new HashMap<>();
        map2.put("couponRequest", couponTypeAddRequest2);

        couponTypeAddRequest.setIssuanceDeadlineAt(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponTypeAddRequest);
        ApiEntity<String> apiEntity = restService.post(FrontURI.SHOPURI+"admin/coupon/add", map, String.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        return "redirect:";
    }
    @GetMapping("list")
    public String allCouponList0(){
        return "redirect:list/0";
    }
    @GetMapping("list/{pageNum}")
    public String allCouponList(Model model, @PathVariable String pageNum){
        ApiEntity<List<Coupon>> apiEntity = restService.get(FrontURI.SHOPURI + "admin/coupon/" + pageNum, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponList", apiEntity.getBody());
        model.addAttribute("memberNo", "");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }

    @GetMapping("list/type/{pageNum}")
    public String allCouponTypeList(Model model, @PathVariable String pageNum){
        ApiEntity<List<CouponType>> apiEntity = restService.get(FrontURI.SHOPURI + "admin/coupon/" + pageNum, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponList", apiEntity.getBody());
        model.addAttribute("pageNum", pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/typeListView");
        return RETURN_PAGE;
    }

    @GetMapping("list/{memberNo}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberNo,
                                   @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOPURI , "admin/coupon/", memberNo , "/" , pageNum.toString());
        ApiEntity<List<Coupon>> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponList", apiEntity.getBody());
        model.addAttribute("memberNo", memberNo+"/");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }

    @GetMapping("detail/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId){
        ApiEntity<CouponDetail> apiEntity = restService.get(FrontURI.SHOPURI + "admin/coupon/"+couponId, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponDetail", apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/detailView");
        return RETURN_PAGE;
    }

    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId){
        ApiEntity<CouponDetail> apiEntity = restService.get(FrontURI.SHOPURI + "admin/coupon/"+couponId, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponDetail", apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/couponUpdateForm");
        return RETURN_PAGE;
    }

    @PostMapping("update/{couponId}")
    public String postUpdateCouponForm(@ModelAttribute CouponTypeAddRequest couponTypeAddRequest,
                                       @PathVariable String couponId){
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponTypeAddRequest);
        ApiEntity<String> apiEntity = restService.put(FrontURI.SHOPURI + "admin/coupon/update/"+couponId, map, String.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        return "redirect:";
    }

    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId){

        String url = buildString(FrontURI.SHOPURI, "admin/coupon/delete/", couponId);
        restService.delete(url);
        return "redirect:";
    }

    @GetMapping("history/{pageNum}")
    public String historyCoupon(Model model, @PathVariable String pageNum){
        String url = buildString(FrontURI.SHOPURI, "admin/coupon/history/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberNo", "");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/historyView");
        return RETURN_PAGE;

    }

    @GetMapping("history/{memberNo}/{pageNum}")
    public String historyCoupon(Model model, @PathVariable String memberNo,
                                @PathVariable String pageNum){
        String url = buildString(FrontURI.SHOPURI, "admin/coupon/history/", memberNo, "/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberNo", memberNo+"/");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/historyView");
        return RETURN_PAGE;
    }

    @GetMapping("issue/{pageNum}")
    public String issueCoupon(Model model, @PathVariable String pageNum){
        String url = buildString(FrontURI.SHOPURI, "admin/coupon/issue/", pageNum);
        ApiEntity<CouponIssue> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>(){});
        model.addAttribute("issueList", apiEntity.getBody());
        model.addAttribute("memberNo", "");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_PAGE;
    }

    @GetMapping("issue/{memberNo}/{pageNum}")
    public String issueCoupon(Model model, @PathVariable String pageNum,
                              @PathVariable String memberNo){
        String url = buildString(FrontURI.SHOPURI, "admin/coupon/issue/", memberNo, "/", pageNum);
        ApiEntity<CouponIssue> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("issueList", apiEntity.getBody());
        model.addAttribute("memberNo", memberNo+"/");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_PAGE;

    }

    @GetMapping("issuing/{couponPageNum}/{memberPageNum}")
    public String issuingCouponForm(@PathVariable Integer couponPageNum,
                                    @PathVariable Integer memberPageNum,
                                    Model model){
        String couponUrl = buildString(FrontURI.SHOPURI, "admin/coupon/couponTypes/", couponPageNum.toString());
        //todo url 나올시 변경
        String memberUrl = buildString(FrontURI.SHOPURI, "", memberPageNum.toString());
        ApiEntity<List<CouponType>> couponApiEntity = restService.get(couponUrl, null, new ParameterizedTypeReference<>(){} );
        //todo 멤버타입 교체해줄 것
        ApiEntity<List<Member>> memberApiEntity = restService.get(memberUrl, null, new ParameterizedTypeReference<>(){} );
        if (!couponApiEntity.isSuccess() || !memberApiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponTypeList", couponApiEntity.getBody());
        model.addAttribute("couponPageNum", couponPageNum);
        model.addAttribute("memberList", memberApiEntity.getBody());
        model.addAttribute("memberPageNum", memberPageNum);

        model.addAttribute(TARGET_VIEW, "coupon/issuing");
        return RETURN_PAGE;
    }

    @PostMapping("issuing/{memberNo}/{couponId}")
    public String memberIssuingCoupon(@PathVariable String couponId, @PathVariable String memberNo,
                                      @RequestParam("couponPageNum") Integer couponPageNum,
                                      @RequestParam("memberPageNum") Integer memberPageNum){
        String url = buildString(FrontURI.SHOPURI, "members/", memberNo, "/coupons/",couponId);
        ApiEntity<String> apiEntity = restService.put(url, null, String.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        return buildString("redirect:issuing/", couponPageNum.toString(), "/", memberPageNum.toString());
    }


    private String buildString(String ... strings){
        StringBuilder builder = new StringBuilder();
        for (String s : strings){
            builder.append(s);
        }
        return builder.toString();
    }
}
