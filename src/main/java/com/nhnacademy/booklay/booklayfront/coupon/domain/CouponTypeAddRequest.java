package com.nhnacademy.booklay.booklayfront.coupon.domain;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
public class CouponTypeAddRequest {

    private String name;
    private Long userId;
    private String typeName;
    private Long amount;
    private Long categoryId;
    private Long minimumUseAmount;
    private Long maximumDiscountAmount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate issuanceDeadlineAt;
    private Boolean isDuplicatable;
}
