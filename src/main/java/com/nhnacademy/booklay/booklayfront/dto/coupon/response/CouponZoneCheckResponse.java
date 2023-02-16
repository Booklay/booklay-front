package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponZoneCheckResponse {
    private LocalDateTime openedAt;
    private LocalDateTime issuanceDeadlineAt;
    private String grade;
}
