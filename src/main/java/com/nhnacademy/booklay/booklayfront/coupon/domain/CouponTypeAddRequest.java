package com.nhnacademy.booklay.booklayfront.coupon.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CouponTypeAddRequest {

    private final String name;
    private final Long typeCode;
    private final Long amount;
    private final Boolean isOrderCoupon;
    private final Long applyItemId;
    private final Long minimumUseAmount;
    private final Long maximumDiscountAmount;
    @Setter
    private LocalDateTime issuanceDeadlineAt;
    private final Boolean isDuplicatable;
    private final Long quantity;


}
