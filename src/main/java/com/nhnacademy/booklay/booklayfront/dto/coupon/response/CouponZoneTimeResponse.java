package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponZoneTimeResponse {
    private LocalDateTime openedAt;
    private LocalDateTime issuanceDeadlineAt;
}
