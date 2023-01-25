package com.nhnacademy.booklay.booklayfront.dto.coupon;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponMemberIssueRequest {

    @NotNull
    private final Long couponId;

    @NotNull
    private final Long memberId;
}
