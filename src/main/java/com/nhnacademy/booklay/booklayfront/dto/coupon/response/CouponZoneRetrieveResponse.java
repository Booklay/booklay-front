package com.nhnacademy.booklay.booklayfront.dto.coupon.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponZoneRetrieveResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private Boolean isBlind;
}
