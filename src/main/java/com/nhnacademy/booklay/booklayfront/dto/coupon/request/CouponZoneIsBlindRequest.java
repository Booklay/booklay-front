package com.nhnacademy.booklay.booklayfront.dto.coupon.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponZoneIsBlindRequest {
    private final Boolean isBlind;
}
