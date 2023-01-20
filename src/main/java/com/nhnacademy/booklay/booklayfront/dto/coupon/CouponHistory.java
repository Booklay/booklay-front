package com.nhnacademy.booklay.booklayfront.dto.coupon;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class CouponHistory {

    private Long couponId;      //쿠폰 번호(대리키)
    private Long userId;        /// 사용자 ID. 쿠폰 사용한 사용자.
    private String code;      //   // 쿠폰 일련번호
    private String name;      //  // 쿠폰 이름
    private Long orderId;       /// 쿠폰을 적용한 주문 번호
    private LocalDate orderedAt;     //   // 쿠폰 사용 일시

    @Builder
    public CouponHistory(Long couponId, Long userId, String code, String name, Long orderId,
                         LocalDate orderedAt) {
        this.couponId = couponId;
        this.userId = userId;
        this.code = code;
        this.name = name;
        this.orderId = orderId;
        this.orderedAt = orderedAt;
    }
}
