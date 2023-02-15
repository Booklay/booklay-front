package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberOwnedCouponResponse {
    private String name;
    private int amount;
    private String couponType;
    private Long usedItemNo;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    private LocalDateTime expiredAt;
    private Boolean isDuplicatable;
    private Boolean isUsed;
    private String reason;
}
