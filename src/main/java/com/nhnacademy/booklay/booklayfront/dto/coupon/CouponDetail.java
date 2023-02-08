package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponDetail {
    private String id;
    private String name;
    private String typeName;
    private int amount;
    private Long applyItemId;
    private String itemName;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    private Boolean isDuplicatable;
    @Setter
    private Boolean isLimited;
    private Long objectFileId;
    private Boolean isOrderCoupon;
}
