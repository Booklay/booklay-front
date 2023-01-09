package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.nhnacademy.booklay.booklayfront.coupon.domain.ApiEntity;
import com.nhnacademy.booklay.booklayfront.coupon.domain.Coupon;
import com.nhnacademy.booklay.booklayfront.coupon.domain.CouponDetail;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("admin/coupon")
public class CouponAdminFrontController {
    private final RestService restService;
    private final FrontURI frontURI;
    @GetMapping("")
    public String adminCouponPage(Model model){
        model.addAttribute("targetUrl", "coupon/empty");
        return "admin/adminPage";
    }

    @GetMapping("create")
    public String createCouponTypeForm(Model model){
        model.addAttribute("targetUrl", "coupon/createCouponTypeForm");
        return "admin/adminPage";
    }

    @PostMapping("create")
    public String postCreateCouponType(@ModelAttribute("CouponTypeAddRequest") CouponTypeAddRequest couponTypeAddRequest){
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
        model.addAttribute("targetUrl", "coupon/listView");
        return "admin/adminPage";
    }

    @GetMapping("list/type/{pageNum}")
    public String allCouponTypeList(Model model, @PathVariable String pageNum){
        ApiEntity<List<Coupon>> apiEntity = restService.get(frontURI.SHOPURI + "admin/coupon/" + pageNum, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponList", apiEntity.getBody());
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("targetUrl", "coupon/typeListView");
        return "admin/adminPage";
    }

    @GetMapping("list/{memberId}")
    public String memberCouponList0(@PathVariable String memberId){
        return "redirect:list/"+memberId+"/0";
    }

    @GetMapping("list/{memberId}/{pageNum}")
    public String memberCouponList(Model model, @PathVariable String memberId,
                                   @PathVariable Integer pageNum){
        String url = buildString(frontURI.SHOPURI , "admin/coupon/", memberId , "/" , pageNum.toString());
        ApiEntity<List<Coupon>> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>(){});
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponList", apiEntity.getBody());
        model.addAttribute("memberId", memberId+"/");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("targetUrl", "coupon/listView");
        return "admin/adminPage";
    }

    @GetMapping("view/{couponId}")
    public String viewCoupon(Model model, @PathVariable String couponId){
        ApiEntity<CouponDetail> apiEntity = restService.get(frontURI.SHOPURI + "admin/coupon/"+couponId, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponDetail", apiEntity.getBody());
        model.addAttribute("targetUrl", "coupon/view");
        return "admin/adminPage";
    }

    @GetMapping("update/{couponId}")
    public String updateCouponForm(Model model, @PathVariable String couponId){
        ApiEntity<CouponDetail> apiEntity = restService.get(frontURI.SHOPURI + "admin/coupon/"+couponId, null, CouponDetail.class);
        if (!apiEntity.isSuccess()){
            return "error";
        }
        model.addAttribute("couponDetail", apiEntity.getBody());
        model.addAttribute("targetUrl", "coupon/couponUpdateForm");
        return "admin/adminPage";
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
        model.addAttribute("targetUrl", "coupon/history");
        return "admin/adminPage";

    }

    @GetMapping("history/{memberId}/{pageNum}")
    public String historyCoupon(Model model, @PathVariable String memberId,
                                @PathVariable String pageNum){
        String url = buildString(frontURI.SHOPURI, "admin/coupon/history/", memberId, "/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberId", memberId+"/");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("targetUrl", "coupon/history");
        return "admin/adminPage";
    }

    @GetMapping("issue/{pageNum}")
    public String issueCoupon(Model model, @PathVariable String pageNum){
        String url = buildString(frontURI.SHOPURI, "admin/coupon/issue/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>(){});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberId", "");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("targetUrl", "coupon/issue");
        return "admin/adminPage";
    }

    @GetMapping("issue/{memberId}/{pageNum}")
    public String issueCoupon(Model model, @PathVariable String pageNum,
                              @PathVariable String memberId){
        String url = buildString(frontURI.SHOPURI, "admin/coupon/issue/", memberId, "/", pageNum);
        ApiEntity<CouponHistory> apiEntity = restService.get(url, null, new ParameterizedTypeReference<>() {});
        model.addAttribute("historyList", apiEntity.getBody());
        model.addAttribute("memberId", memberId+"/");
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("targetUrl", "coupon/issue");
        return "admin/adminPage";

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

        model.addAttribute("targetUrl", "coupon/issuing");
        return "admin/adminPage";
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
