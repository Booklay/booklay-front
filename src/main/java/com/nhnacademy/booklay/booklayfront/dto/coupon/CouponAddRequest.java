package com.nhnacademy.booklay.booklayfront.dto.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @NotNull
    private final Long applyItemId;
    @NotNull
    private final Long minimumUseAmount;
    private final Long maximumDiscountAmount;
    @Setter
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issuanceDeadlineAt;
    @NotNull
    private final Boolean isDuplicatable;
    @NotNull
    private final Boolean isLimited;


}
