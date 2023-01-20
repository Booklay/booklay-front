package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponSettingAddRequest {
    private final Integer settingType;
    private final Long couponTemplateNo;
    private final Long memberGrade;
}
