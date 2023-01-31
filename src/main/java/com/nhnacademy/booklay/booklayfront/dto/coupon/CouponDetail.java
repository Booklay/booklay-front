package com.nhnacademy.booklay.booklayfront.dto.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponDetail {
    @Setter
    private String id;
    private String name;
    private Long userId;
    private String typeName;
    private Long amount;
    private Long categoryId;
    private Long productId;
    private Long minimumUseAmount;
    private Long maximumDiscountAmount;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime issuanceDeadlineAt;
    private Boolean isDuplicatable;
    private String couponImagePath;
    private Boolean isLimited;
}
