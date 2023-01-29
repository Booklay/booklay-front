package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_ISSUE_LIST;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_MEMBER_NO;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.PAGE_NUM;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.RETURN_ADMIN_PAGE;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponIssue;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponIssueRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponMemberIssueRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import java.util.Map;
import javax.validation.Valid;
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
    

    @ModelAttribute("navHead")
    public String addNavHead() {
        return "coupon/couponFragments/couponNavHead";
    }

    @GetMapping("issue/{pageNum}")
    public String issueCoupon(Model model, @PathVariable Integer pageNum) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, "/issue/", pageNum.toString());
        ApiEntity<CouponIssue> apiEntity =
            restService.get(url, null, new ParameterizedTypeReference<>() {
            });
        model.addAttribute(ATTRIBUTE_NAME_ISSUE_LIST, apiEntity.getBody());
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, "");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_ADMIN_PAGE;
    }

    @GetMapping("issue/{memberNo}/{pageNum}")
    public String memberIssueCoupon(Model model, @PathVariable Integer pageNum,
                                    @PathVariable String memberNo) {
        couponRestApiModelSettingService.setCouponIssueHistoryToModelByPageAndMemberNo(pageNum, memberNo, model);

        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);
        model.addAttribute(TARGET_VIEW, "coupon/issueView");
        return RETURN_ADMIN_PAGE;

    }

    @GetMapping("/issue")
    public String issueCouponForm(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/issueCouponForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("/issue")
    public String issueCouponCreate(@Valid @ModelAttribute CouponIssueRequest couponIssueRequest) {
        Map<String, Object> map = objectMapper.convertValue(couponIssueRequest, Map.class);
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "issue");
        restService.post(url, map, String.class);

        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("/member/issue")
    public String issueCouponToMemberForm(Model model) {
        model.addAttribute(TARGET_VIEW, "coupon/issueCouponToMemberForm");
        return RETURN_ADMIN_PAGE;
    }

    @PostMapping("/member/issue")
    public String issueCouponToMember(@Valid @ModelAttribute CouponMemberIssueRequest couponRequest) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "members/issue");
        Map<String, Object> map = objectMapper.convertValue(couponRequest, Map.class);
        restService.post(url, map, String.class);
        return RETURN_PAGE_COUPON_LIST;
    }

    @GetMapping("/issue-history")
    public String issueHistoryList(@RequestParam(value="pageNum", defaultValue = "0") Integer pageNum, Model model) {
        couponRestApiModelSettingService.setCouponIssueHistoryToModelByPage(pageNum, model);
        return "redirect:/";
    }

}
