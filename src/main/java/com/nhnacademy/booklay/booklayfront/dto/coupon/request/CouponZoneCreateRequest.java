package com.nhnacademy.booklay.booklayfront.dto.coupon.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 쿠폰존에 쿠폰을 등록하기 위한 객체.
 * openedAt 이 시간부터 쿠폰 발급이 가능합니다.
 * issuanceDeadlineAt 이 시간까지 쿠폰 발급이 가능합니다.
 * expiredAt 이 때까지 쿠폰을 사용할 수 있습니다.
 */
@Getter
@AllArgsConstructor
public class CouponZoneCreateRequest {
    @NotNull
    private final Long couponId;

    @NotNull
    private final String description;

    @NotNull
    private final String grade;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime openedAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issuanceDeadlineAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;

    @Setter
    private Boolean isBlind;
}
