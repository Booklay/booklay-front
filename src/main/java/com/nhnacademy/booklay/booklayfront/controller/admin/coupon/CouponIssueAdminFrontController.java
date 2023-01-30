package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponIssueRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponMemberIssueRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
public class CouponIssueAdminFrontController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    private final CouponRestApiModelSettingService couponRestApiModelSettingService;

    private static final String RETURN_PAGE_COUPON_LIST = "redirect:/admin/coupons/list/0";
    private static final String COUPON_ISSUE_HTML_PATH = "coupon/issue/";

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    @GetMapping("/issue")
    public String issueCouponForm(Model model) {
        model.addAttribute(TARGET_VIEW, COUPON_ISSUE_HTML_PATH + "issueCouponForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("/issue")
    public String issueCouponCreate(@Valid @ModelAttribute CouponIssueRequest couponIssueRequest) {
        Map<String, Object> map = objectMapper.convertValue(couponIssueRequest, Map.class);
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "issue");
        restService.post(url, map, String.class);
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("issue/{pageNum}")
    public String issueCoupon(Model model, @PathVariable Integer pageNum) {
        couponRestApiModelSettingService.setCouponIssueToModelByPage(pageNum, model);
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, COUPON_ISSUE_HTML_PATH+"issueView");
        return RETURN_ADMIN_PAGE;
    }

    @GetMapping("issue/{memberNo}/{pageNum}")
    public String memberIssueCoupon(Model model, @PathVariable Integer pageNum,
                                    @PathVariable String memberNo) {
        couponRestApiModelSettingService.setCouponIssueToModelByPageAndMemberNo(pageNum, memberNo, model);
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, COUPON_ISSUE_HTML_PATH+"issueView");
        return RETURN_ADMIN_PAGE;

    }

    @GetMapping("/member/issue")
    public String issueCouponToMemberForm(Model model) {
        model.addAttribute(TARGET_VIEW, COUPON_ISSUE_HTML_PATH + "issueCouponToMemberForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("/member/issue")
    public String issueCouponToMember(@Valid @ModelAttribute CouponMemberIssueRequest couponRequest) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "members/issue");
        Map<String, Object> map = objectMapper.convertValue(couponRequest, Map.class);
        restService.post(url, map, String.class);
        return RETURN_PAGE_COUPON_LIST;
    }

    /**
     * 관리자의 발급 기록 조회.
     *
     */
    @GetMapping("/issue-history")
    public String issueHistoryList(@RequestParam(value="pageNum", defaultValue = "0") Integer pageNum, Model model) {
        couponRestApiModelSettingService.setCouponIssueHistoryToModelByPage(pageNum, model);
        model.addAttribute(TARGET_VIEW, COUPON_ISSUE_HTML_PATH + "issueHistoryView");
        return RETURN_ADMIN_PAGE;
    }

}
