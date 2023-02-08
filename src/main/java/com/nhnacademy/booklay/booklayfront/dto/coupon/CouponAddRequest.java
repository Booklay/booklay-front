package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CouponAddRequest {
    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    private Long typeCode;

    @NotNull
    private Long amount;

    @NotNull
    private Boolean isOrderCoupon;

    private Long applyItemId;

    @NotNull
    private Long minimumUseAmount;

    @NotNull
    private Long maximumDiscountAmount;

    @NotNull
    private Boolean isDuplicatable;

    @NotNull
    private Boolean isLimited;
}
