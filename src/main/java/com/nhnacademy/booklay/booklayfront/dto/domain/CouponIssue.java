package com.nhnacademy.booklay.booklayfront.dto.domain;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class CouponIssue {

    private Long couponId;      //쿠폰 번호(대리키)
    private Long userId;        /// 사용자 ID. 발급 대상 사용자
    private String code;      //   // 쿠폰 일련번호
    private String name;      //  // 쿠폰 이름
    private LocalDate issuedAt;     //   // 발급일

    @Builder
    public CouponIssue(Long couponId, Long userId, String code, String name, LocalDate issuedAt) {
        this.couponId = couponId;
        this.userId = userId;
        this.code = code;
        this.name = name;
        this.issuedAt = issuedAt;
    }
}
