package com.nhnacademy.booklay.booklayfront.dto.coupon.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponZoneMemberIssueRequest {

    @NotNull
    private final Long couponId;

    @NotNull
    private final Long memberId;
}