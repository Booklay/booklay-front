package com.nhnacademy.booklay.booklayfront.coupon.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponIssue {

    private Long couponId;      //쿠폰 번호(대리키)
    private Long userId;        /// 사용자 ID. 발급 대상 사용자
    private Long code;      //   // 쿠폰 일련번호
    private Long name;      //  // 쿠폰 이름
    private Long issuedAt;     //   // 발급일
}
