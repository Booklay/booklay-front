package com.nhnacademy.booklay.booklayfront.dto.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CouponAddRequest {

    private final String name;
    private final Long typeCode;
    private final Long amount;
    private final Boolean isOrderCoupon;
    private final Long applyItemId;
    private final Long minimumUseAmount;
    private final Long maximumDiscountAmount;
    @Setter
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime issuanceDeadlineAt;
    private final Boolean isDuplicatable;
    private final Long quantity;


}
