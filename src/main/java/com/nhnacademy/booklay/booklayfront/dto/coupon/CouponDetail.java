package com.nhnacademy.booklay.booklayfront.dto.coupon;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CouponDetail {
    private String id;
    private String name;
    private String typeName;
    private int amount;
    private Long applyItemId;
    private String itemName;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime issuanceDeadlineAt;
    private Boolean isDuplicatable;
    @Setter
    private Boolean isLimited;
    private Long objectFileId;
    private Boolean isOrderCoupon;
    private int validateTerm;
}
