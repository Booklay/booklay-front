package com.nhnacademy.booklay.booklayfront.coupon.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class CouponDetail {
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

    @Builder
    public CouponDetail(String couponName, Long userId, String typeName, Long amount, Long categoryId, Long productId, Long minimumUseAmount, Long maximumDiscountAmount, LocalDate issuanceDeadlineAt, Boolean isDuplicatable) {
        this.couponName = couponName;
        this.userId = userId;
        this.typeName = typeName;
        this.amount = amount;
        this.categoryId = categoryId;
        this.productId = productId;
        this.minimumUseAmount = minimumUseAmount;
        this.maximumDiscountAmount = maximumDiscountAmount;
        this.issuanceDeadlineAt = issuanceDeadlineAt;
        this.isDuplicatable = isDuplicatable;
    }
}
