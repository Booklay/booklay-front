package com.nhnacademy.booklay.booklayfront.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponDetail {
    @Setter
    private String couponId;
    private String couponName;
    private Long userId;
    private String typeName;
    private Long amount;
    private Long categoryId;
    private Long productId;
    private Long minimumUseAmount;
    private Long maximumDiscountAmount;
    private LocalDate issuanceDeadlineAt;
    private Boolean isDuplicatable;
    private String couponImagePath;

}
