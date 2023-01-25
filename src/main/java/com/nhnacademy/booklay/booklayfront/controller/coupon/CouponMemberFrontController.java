package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.dto.domain.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.domain.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.domain.CouponDetail;
import com.nhnacademy.booklay.booklayfront.dto.domain.CouponHistory;
import com.nhnacademy.booklay.booklayfront.dto.domain.CouponIssue;
import com.nhnacademy.booklay.booklayfront.dto.domain.FrontURI;
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

import static com.nhnacademy.booklay.booklayfront.dto.domain.ControllerStrings.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("member/coupon")
public class CouponMemberFrontController {
    private final RestService restService;
    private static final String RETURN_PAGE = "mypage/myPage";
    private static final String REST_PRE_FIX = "/shop/v1/member/coupon/";

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    @GetMapping("")
    public String memberCouponPage(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/empty");
        return RETURN_PAGE;
    }

    @GetMapping("list")
    public String allCouponList0() {
        return "redirect:list/0";
    }

    @GetMapping("list/{pageNum}")
    public String allCouponList(Model model, @PathVariable Integer pageNum) {
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, "list/", pageNum.toString());
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        ApiEntity<List<Coupon>> apiEntity =
            restService.get(url, map, new ParameterizedTypeReference<>() {
            });
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/listView");
        return RETURN_PAGE;
    }

    @GetMapping("detail/{couponId}")
    public String couponDetail(Model model, @PathVariable String couponId) {
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, "detail/", couponId);
        ApiEntity<CouponDetail> apiEntity = restService.get(url, null, CouponDetail.class);
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_COUPON_DETAIL, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/detailView");
        return RETURN_PAGE;
    }

    @GetMapping("history/{pageNum}")
    public String couponHistory(Model model, @PathVariable Integer pageNum) {
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, "history/", pageNum.toString());
        ApiEntity<List<CouponHistory>> apiEntity =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_HISTORY_LIST, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/historyView");
        return RETURN_PAGE;
    }

    @GetMapping("issue/{pageNum}")
    public String couponIssuing(Model model, @PathVariable Integer pageNum) {
        String url = buildString(FrontURI.SHOP_URI, REST_PRE_FIX, "issue/", pageNum.toString());
        ApiEntity<List<CouponIssue>> apiEntity =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });
        if (!apiEntity.isSuccess()) {
            return ERROR;
        }
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody());
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_PAGE;
    }


    private String buildString(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String s : strings) {
            builder.append(s);
        }
        return builder.toString();
    }
}
