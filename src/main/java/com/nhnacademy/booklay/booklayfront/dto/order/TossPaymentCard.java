package com.nhnacademy.booklay.booklayfront.dto.order;

import lombok.Getter;

@Getter
public class TossPaymentCard {
    private String issuerCode;
    private String acquirerCode;
    private String number;
    private Integer installmentPlanMonths;
    private Boolean isInterestFree;
    private String interestPayer;
    private String approveNo;
    private Boolean useCardPoint;
    private String cardType;
    private String ownerType;
    private String acquireStatus;
}
