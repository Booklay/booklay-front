package com.nhnacademy.booklay.booklayfront.dto.coupon;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CouponTypeAddRequest {
    @NotNull
    private final Long id;
    @NotNull
    private final String name;

}
