package com.nhnacademy.booklay.booklayfront.dto.domain;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponIssueRequest {
    @NotNull
    private final Long couponId;

    @NotNull
    private final int quantity;
}
