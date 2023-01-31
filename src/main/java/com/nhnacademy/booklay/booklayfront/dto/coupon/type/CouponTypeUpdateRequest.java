package com.nhnacademy.booklay.booklayfront.dto.coupon.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponTypeUpdateRequest {
    @NotNull
    private Long id;
    @NotNull
    private String name;

}
