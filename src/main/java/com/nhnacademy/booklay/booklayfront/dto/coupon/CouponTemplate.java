package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponTemplate {
    private Long id;
    private String name;
    private Boolean isOrderCoupon;
    private Long applyItemId;
    private int amount;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    private Boolean isDuplicatable;
}
