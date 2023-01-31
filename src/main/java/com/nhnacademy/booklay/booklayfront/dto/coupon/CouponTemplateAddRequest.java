package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponTemplateAddRequest {
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    private Long typeCode;
    @NotNull
    private Long amount;
    @NotNull
    private Boolean isOrderCoupon;
    @NotNull
    private Long applyItemId;
    @NotNull
    private Long minimumUseAmount;
    private Long maximumDiscountAmount;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issuingDeadLine;
    private Integer validateTerm;
    @NotNull
    private Boolean isDuplicatable;
}
