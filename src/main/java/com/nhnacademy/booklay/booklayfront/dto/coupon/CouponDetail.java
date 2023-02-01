package com.nhnacademy.booklay.booklayfront.dto.coupon;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
    private LocalDateTime issuanceDeadlineAt;
    private Boolean isDuplicatable;
    private Boolean isLimited;
    private Long objectFileId;
    private Boolean isOrderCoupon;
}
