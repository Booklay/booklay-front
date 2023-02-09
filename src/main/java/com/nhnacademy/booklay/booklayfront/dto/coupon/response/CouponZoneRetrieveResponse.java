package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class CouponZoneRetrieveResponse {
    private Long id;
    private Long couponId;
    private Long fileId;
    private String name;
    private String description;
    @Setter
    private String grade;
    private LocalDateTime openedAt;
    private LocalDateTime issuanceDeadlineAt;
    private LocalDateTime expiredAt;
    private Boolean isBlind;
}
