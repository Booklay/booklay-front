package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.nhnacademy.booklay.booklayfront.coupon.domain.ApiEntity;
import com.nhnacademy.booklay.booklayfront.coupon.domain.Coupon;
import com.nhnacademy.booklay.booklayfront.coupon.domain.CouponHistory;
import com.nhnacademy.booklay.booklayfront.coupon.domain.CouponType;
import com.nhnacademy.booklay.booklayfront.coupon.domain.CouponTypeAddRequest;
import com.nhnacademy.booklay.booklayfront.coupon.domain.FrontURI;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CouponAdminFrontController {
    private final RestService restService;
    private final FrontURI frontURI;
    @GetMapping("")
    public String adminCouponPage(){
        return "admin/coupon";
    }

    @GetMapping("create")
    public String createCouponTypeForm(){
        return "admin/coupon/createCouponTypeForm";
    }

    @PostMapping("create")
    public String postCreateCouponType(@ModelAttribute CouponTypeAddRequest couponTypeAddRequest){
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponTypeAddRequest);
        ApiEntity<String>
            apiEntity = restService.post(frontURI.SHOPURI+"admin/coupon/add", map, String.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        return "redirect:";
    }

    @GetMapping("list/{pageNum}")
    public String allCouponList(Model model, @PathVariable String pageNum){
        ApiEntity<List<Coupon>> apiEntity = restService.get(frontURI.SHOPURI + "admin/coupon/" + pageNum, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponList", apiEntity.getBody());
        model.addAttribute("memberId", "");
        model.addAttribute("pageNum", pageNum);
        return "admin/coupon/listView";
    }

    @GetMapping("list/{memberId}")
    public String memberCouponList0(@PathVariable String memberId){
        return "redirect:list/"+memberId+"/0";
    }

    @GetMapping("list/{memberId}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberId,
                                   @PathVariable Integer pageNum){
        ApiEntity<List<Coupon>> apiEntity = restService.get(frontURI.SHOPURI + "admin/coupon/"+ memberId + "/" + pageNum, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponList", apiEntity.getBody());
        model.addAttribute("memberId", memberId+"/");
        model.addAttribute("pageNum", pageNum);
        return "admin/coupon/listView";
    }

    @GetMapping("view/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId){
        ApiEntity<Coupon> apiEntity = restService.get(frontURI.SHOPURI + "admin/coupon/"+couponId, null, Coupon.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("coupon", apiEntity.getBody());
        return "admin/coupon/view";
    }

    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId){
        ApiEntity<Coupon> apiEntity = restService.get(frontURI.SHOPURI + "admin/coupon/"+couponId, null, Coupon.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("coupon", apiEntity.getBody());
        return "admin/coupon/updateForm";
    }

    @PostMapping("update/{couponId}")
    public String postUpdateCouponForm(@ModelAttribute CouponTypeAddRequest couponTypeAddRequest,
                                       @PathVariable String couponId){
        Map<String, Object> map = new HashMap<>();
        map.put("couponRequest", couponTypeAddRequest);
        ApiEntity<String> apiEntity = restService.put(frontURI.SHOPURI + "admin/coupon/update/"+couponId, map, String.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        return "redirect:";
    }

    @GetMapping("delete/{couponId}")
    public String deleteCoupon(@PathVariable String couponId){

        String url = buildString(frontURI.SHOPURI, "admin/coupon/delete/", couponId);
        restService.delete(url);
        return "redirect:";
    }

    @GetMapping("history/{pageNum}")
    public String historyCoupon(Model model, @PathVariable String pageNum){
        String url = buildString(frontURI.SHOPURI, "admin/coupon/history/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberId", "");
        model.addAttribute("pageNum", pageNum);

        return "admin/coupon/history";
    }

    @GetMapping("history/{memberId}/{pageNum}")
    public String historyCoupon(Model model, @PathVariable String memberId,
                                @PathVariable String pageNum){
        String url = buildString(frontURI.SHOPURI, "admin/coupon/history/", memberId, "/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberId", memberId+"/");
        model.addAttribute("pageNum", pageNum);
        return "admin/coupon/history";
    }

    @GetMapping("issue/{pageNum}")
    public String issueCoupon(Model model, @PathVariable String pageNum){
        String url = buildString(frontURI.SHOPURI, "admin/coupon/issue/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberId", "");
        model.addAttribute("pageNum", pageNum);

        return "admin/coupon/issue";
    }

    @GetMapping("issue/{memberId}/{pageNum}")
    public String issueCoupon(Model model, @PathVariable String pageNum,
                              @PathVariable String memberId){
        String url = buildString(frontURI.SHOPURI, "admin/coupon/issue/", memberId, "/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberId", "");
        model.addAttribute("pageNum", pageNum);

        return "admin/coupon/issue";
    }

    @GetMapping("issuing/{couponPageNum}/{memberPageNum}")
    public String issuingCouponForm(@PathVariable Integer couponPageNum,
                                    @PathVariable Integer memberPageNum,
                                    Model model){
        String couponUrl = buildString(frontURI.SHOPURI, "admin/coupon/couponTypes/", couponPageNum.toString());
        //todo url 나올시 변경
        String memberUrl = buildString(frontURI.SHOPURI, "", memberPageNum.toString());
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

        return "admin/coupon/issuing";
    }

    @PostMapping("issuing/{memberId}/{couponId}")
    public String memberIssuingCoupon(@PathVariable String couponId, @PathVariable String memberId,
                                      @RequestParam("couponPageNum") Integer couponPageNum,
                                      @RequestParam("memberPageNum") Integer memberPageNum){
        String url = buildString(frontURI.SHOPURI, "members/", memberId, "/coupons/",couponId);
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
