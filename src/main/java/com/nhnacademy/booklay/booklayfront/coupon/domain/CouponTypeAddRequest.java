package com.nhnacademy.booklay.booklayfront.coupon.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
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

//    @Builder
//    public CouponTypeAddRequest(String name, Long userId, String typeName, Long amount, Long categoryId, Long minimumUseAmount, Long maximumDiscountAmount, LocalDateTime issuanceDeadlineAt, Boolean isDuplicatable, Long issueAmount) {
//        this.name = name;
//        this.userId = userId;
//        this.typeName = typeName;
//        this.amount = amount;
//        this.categoryId = categoryId;
//        this.minimumUseAmount = minimumUseAmount;
//        this.maximumDiscountAmount = maximumDiscountAmount;
//        this.issuanceDeadlineAt = issuanceDeadlineAt;
//        this.isDuplicatable = isDuplicatable;
//        this.issueAmount = issueAmount;
//    }

}
