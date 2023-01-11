package com.nhnacademy.booklay.booklayfront.coupon.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@RequiredArgsConstructor
public class CouponTypeAddRequest {

    private final String name;
    private final Long userId;
    private final String typeName;
    private final Long amount;
    private final Long categoryId;
    private final Long minimumUseAmount;
    private final Long maximumDiscountAmount;
    @Setter
    private LocalDateTime issuanceDeadlineAt;
    private final Boolean isDuplicatable;
    private final Long issueAmount;


}
