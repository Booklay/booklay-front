package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public class CouponSetting {
    @Setter
    private String id;
    private Integer settingType;
    private Long couponTemplateNo;
    private Long memberGrade;
}
