package com.nhnacademy.booklay.booklayfront.dto.coupon.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@RequiredArgsConstructor
public class CouponMemberIssueRequest {

    @NotNull
    private final Long couponId;

    @NotNull
    private final Long memberId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expiredAt;
}
