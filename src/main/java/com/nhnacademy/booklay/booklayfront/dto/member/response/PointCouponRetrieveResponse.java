package com.nhnacademy.booklay.booklayfront.dto.member.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PointCouponRetrieveResponse {
    private Long couponId;
    private String name;
    private int amount;
}
