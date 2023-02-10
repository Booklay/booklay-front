package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.MemberOwnedCouponResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponDetail;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponHistory;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponIssue;
import com.nhnacademy.booklay.booklayfront.dto.coupon.FrontURI;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
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
import org.springframework.web.bind.annotation.RequestParam;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/coupon")
public class CouponMemberFrontController extends BaseController {
    private final String gatewayIp;
    private final CouponRestApiModelSettingService modelSettingService;

    private static final String MEMBER_COUPON_RESOURCE = "mypage/coupon/";
    private static final String RETURN_PAGE_OWNED_LIST = "redirect:/member/coupon";

    /**
     * 사용자가 소유한 쿠폰을 조회하여 보여줍니다.
     */
    @GetMapping
    public String memberCouponPage(Model model, MemberInfo memberInfo,
                                   @RequestParam(value = "page", defaultValue = "0") int pageNum) {
        Long memberNo = memberInfo.getMemberNo();
        modelSettingService.setOwnedCouponToModelByMember(pageNum, memberNo, model);

        return MEMBER_COUPON_RESOURCE + "ownedCouponListView";
    }

//    /**
//     * 쿠폰 상세 보기.
//     * @param model
//     * @param couponId
//     * @return
//     */
//    @GetMapping("detail/{couponId}")
//    public String couponDetail(Model model, @PathVariable String couponId) {
//
//    }
//
//    @GetMapping("history/{pageNum}")
//    public String couponHistory(Model model, @PathVariable Integer pageNum) {
//
//    }

}
