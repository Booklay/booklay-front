package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CouponZoneRetrieveResponse {
    private Long id;
    private Long couponId;
    private String name;
    private String description;
    private LocalDateTime openedAt;
    private LocalDateTime issuanceDeadlineAt;
    // TODO 등급 넣기
    private Boolean isBlind;
}
