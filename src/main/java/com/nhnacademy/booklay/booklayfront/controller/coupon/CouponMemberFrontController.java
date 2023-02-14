package com.nhnacademy.booklay.booklayfront.controller.coupon;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.exception.LoginEssentialException;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.CouponRestApiModelSettingService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/mypage/coupon")
public class CouponMemberFrontController {
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

}
