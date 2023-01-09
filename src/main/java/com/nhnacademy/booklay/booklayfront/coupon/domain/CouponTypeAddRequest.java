package com.nhnacademy.booklay.booklayfront.coupon.domain;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate issuanceDeadlineAt;
    private final Boolean isDuplicatable;

    private final Long issueAmount;
}
