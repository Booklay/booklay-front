package com.nhnacademy.booklay.booklayfront.controller.admin.coupon;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ADMIN_COUPON_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ATTRIBUTE_NAME_MEMBER_NO;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_COUPON;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.PAGE_NUM;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.RETURN_ADMIN_PAGE;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponIssueRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.request.CouponMemberIssueRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 쿠폰 발급 및 발급 기록 조회에 대한 요청을 받습니다.
 * @author 김승혜
 */
@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
public class CouponIssueAdminFrontController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    private final CouponRestApiModelSettingService couponRestApiModelSettingService;

    /**
     * 수량만큼의 쿠폰 발급 폼.
     *
     */
    @GetMapping("/issue")
    public String issueCouponForm() {
        return "admin/coupon/issue/issueCouponForm";
    }

    /**
     * 쿠폰을 quantity만큼 생성 요청 후, 쿠폰 발급 페이지 보이기.
     * @param couponIssueRequest 생성하려는 쿠폰의 couponId와 수량
     *
     */
    @PostMapping("/issue")
    public String issueCouponCreate(@Valid @ModelAttribute CouponIssueRequest couponIssueRequest) {
        Map<String, Object> map = objectMapper.convertValue(couponIssueRequest, Map.class);
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "issue");
        restService.post(url, map, String.class);

        return "redirect:/admin/coupons/issue-history";
    }

    /**
     * 특정 사용자에게 쿠폰을 발급하기 위한 폼.
     */
    @GetMapping("/member/issue")
    public String issueCouponToMemberForm() {
        return "admin/coupon/issue/issueCouponToMemberForm";
    }

    /**
     * 특정 사용자에게 쿠폰 발급.
     * @param couponRequest 발급하려는 couponId, 특정 발급 대상 사용자 memberId
     */
    @PostMapping("/member/issue")
    public String issueCouponToMember(@Valid @ModelAttribute CouponMemberIssueRequest couponRequest) {
        String url = buildString(gatewayIp, DOMAIN_PREFIX_COUPON, ADMIN_COUPON_REST_PREFIX, "members/issue");
        Map<String, Object> map = objectMapper.convertValue(couponRequest, Map.class);
        restService.post(url, map, String.class);

        return "redirect:/admin/coupons/member/issue";
    }

    /**
     * 발급한 쿠폰 리스트 조회.
     */
    @GetMapping("issue-history")
    public String getCouponIssueHistory(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                            Model model) {
        couponRestApiModelSettingService.setCouponIssueToModelByPage(pageNum, model);

        model.addAttribute(PAGE_NUM, pageNum);

        return "admin/coupon/issue/issueHistoryView";
    }

    @GetMapping("issue-history/{memberNo}")
    public String memberIssueCoupon(Model model, @RequestParam(value = "page", defaultValue = "0") int pageNum,
                                    @PathVariable String memberNo) {
        couponRestApiModelSettingService.setCouponIssueToModelByPageAndMemberNo(pageNum, memberNo, model);
        model.addAttribute(ATTRIBUTE_NAME_MEMBER_NO, memberNo + "/");
        model.addAttribute(PAGE_NUM, pageNum);

        return RETURN_ADMIN_PAGE;
    }

    /**
     * 사용된 쿠폰 리스트 조회
     */
    @GetMapping("/use-history")
    public String getCouponUsedHistory(@RequestParam(value = "page", defaultValue = "0") int pageNum,
                                       Model model) {
        couponRestApiModelSettingService.setCouponUseHistoryToModelByPage(pageNum, model);

        return "admin/coupon/issue/useHistoryView";
    }
}
