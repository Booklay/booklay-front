package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.nhnacademy.booklay.booklayfront.coupon.domain.*;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("member/coupon")
public class CouponMemberFrontController {
    private final RestService restService;
    private static final String TARGET_VIEW = "targetUrl";
    private static final String RETURN_PAGE = "member/memberPage";
    @GetMapping("")
    public String memberCouponPage(Model model){
        model.addAttribute(TARGET_VIEW, "coupon/empty");
        return RETURN_PAGE;
    }
    @ModelAttribute("navHead")
    public String addNavHead(){
        return "coupon/couponFragments/couponNavHead";
    }
    @GetMapping("list")
    public String allCouponList0(){
        return "redirect:list/0";
    }
    @GetMapping("list/{pageNum}")
    public String allCouponList(Model model, @PathVariable Integer pageNum){

        String url = buildString(FrontURI.SHOPURI, "member/coupon/list/", pageNum.toString());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("memberId", "0");
        ApiEntity<List<Coupon> > apiEntity = restService.get(url, map, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponList", apiEntity.getBody());
        model.addAttribute("memberNo", "");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }

    @GetMapping("detail/{couponId}")
    public String couponDetail(Model model, @PathVariable String couponId){
        String url = buildString(FrontURI.SHOPURI, "member/coupon/detail/", couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponDetail", apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/detailView");
        return RETURN_PAGE;
    }

    @GetMapping("history/{pageNum}")
    public String couponHistory(Model model, @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOPURI, "member/coupon/history/", pageNum.toString());
        ApiEntity<List<CouponHistory>> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/historyView");
        return RETURN_PAGE;
    }

    @GetMapping("issue/{pageNum}")
    public String couponIssuing(Model model, @PathVariable Integer pageNum){
        String url = buildString(FrontURI.SHOPURI, "member/coupon/issue/", pageNum.toString());
        ApiEntity<List<CouponIssue>> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("issueList", apiEntity.getBody());
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
