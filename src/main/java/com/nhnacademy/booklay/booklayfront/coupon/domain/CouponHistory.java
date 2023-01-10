package com.nhnacademy.booklay.booklayfront.coupon.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponHistory {

        private Long couponId;      //쿠폰 번호(대리키)
        private Long userId;        /// 사용자 ID. 쿠폰 사용한 사용자.
        private Long code;      //   // 쿠폰 일련번호
        private Long name;      //  // 쿠폰 이름
        private Long orderId;       /// 쿠폰을 적용한 주문 번호
        private Long orderedAt;     //   // 쿠폰 사용 일시

}
