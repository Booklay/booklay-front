package com.nhnacademy.booklay.booklayfront.dto.coupon.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@AllArgsConstructor
public class CouponZoneCreateRequest {
    @NotNull
    private final Long couponId;

    @NotNull
    private final String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime openedAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issuanceDeadlineAt;

    @Setter
    private Boolean isBlind;
}
