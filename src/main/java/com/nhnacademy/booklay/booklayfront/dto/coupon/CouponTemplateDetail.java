package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponTemplateDetail {
    @Setter
    private String id;
    private String imagePath;
    private Long typeCode;
    private String name;
    private Boolean isOrderCoupon;
    private Long applyItemId;
    private int amount;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    private LocalDateTime issuanceDeadlineAt;
    private Integer validateTerm;
    private Boolean isDuplicatable;

}
