package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class MemberOwnedCouponResponse {
    private String name;
    private int amount;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    private LocalDateTime expiredAt;
    private Boolean isDuplicatable;
}
