package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponUsedHistoryResponse {
    private String memberId;
    private String couponName;
    private Long discountPrice;
    private LocalDateTime orderedAt;
    private LocalDateTime issuedAt;
}
