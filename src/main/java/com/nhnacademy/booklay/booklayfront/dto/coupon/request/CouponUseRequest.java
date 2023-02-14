package com.nhnacademy.booklay.booklayfront.dto.coupon.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CouponUseRequest {

    List<CouponUsingDto> productCouponList = new ArrayList<>();
    List<CouponUsingDto> categoryCouponList = new ArrayList<>();
}
