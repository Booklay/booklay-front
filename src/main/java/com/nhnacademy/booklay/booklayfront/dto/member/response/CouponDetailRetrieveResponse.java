package com.nhnacademy.booklay.booklayfront.dto.member.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CouponDetailRetrieveResponse {
    private Long id;
    private String name;
    private String typeName;
    private int amount;
    private Long applyItemId;
    private String itemName;
    private int minimumUseAmount;
    private int maximumDiscountAmount;
    private LocalDateTime issuanceDeadlineAt;
    private Boolean isDuplicatable;
    private Boolean isLimited;
    private Long objectFileId;
    private Boolean isOrderCoupon;
    private int validateTerm;
}
