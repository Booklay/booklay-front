package com.nhnacademy.booklay.booklayfront.dto.coupon;

import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@RequiredArgsConstructor
public class CouponAddRequest {
    @NotBlank
    @Size(max = 100)
    private final String name;

    @NotNull
    private final Long typeCode;

    @NotNull
    private final Long amount;

    @NotNull
    private final Boolean isOrderCoupon;

    private final Long applyItemId;

    @NotNull
    private final Long minimumUseAmount;

    @NotNull
    private final Long maximumDiscountAmount;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private final LocalDateTime issuanceDeadlineAt;

    @NotNull
    private final Boolean isDuplicatable;

    @NotNull
    private final Boolean isLimited;

    @NotNull
    private final int validateTerm;
}
