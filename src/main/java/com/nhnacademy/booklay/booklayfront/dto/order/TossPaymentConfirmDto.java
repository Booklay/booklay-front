package com.nhnacademy.booklay.booklayfront.dto.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TossPaymentConfirmDto {
    private final String paymentKey;
    private final String orderId;
    private final Integer amount;
}
