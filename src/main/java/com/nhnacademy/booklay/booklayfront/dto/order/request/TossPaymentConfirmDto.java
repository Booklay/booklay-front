package com.nhnacademy.booklay.booklayfront.dto.order.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TossPaymentConfirmDto {
    private final String paymentKey;
    private final String orderId;
    private final Integer amount;

    public boolean isPaymentAmountZero(){
        return amount == 0;
    }
    public boolean isPaymentAmountOverZero(){
        return amount > 0;
    }
}
