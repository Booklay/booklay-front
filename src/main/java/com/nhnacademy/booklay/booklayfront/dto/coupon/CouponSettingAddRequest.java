package com.nhnacademy.booklay.booklayfront.dto.coupon;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponSettingAddRequest {
    @NotNull
    private final Integer settingType;
    @NotNull
    private final Long couponTemplateNo;
    @NotNull
    private final Long memberGrade;
}
