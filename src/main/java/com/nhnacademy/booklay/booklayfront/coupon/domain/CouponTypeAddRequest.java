package com.nhnacademy.booklay.booklayfront.coupon.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponTypeAddRequest {
    private final Long id;
    private final String name;

}
