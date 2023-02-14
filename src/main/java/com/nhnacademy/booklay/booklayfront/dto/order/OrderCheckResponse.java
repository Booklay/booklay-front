package com.nhnacademy.booklay.booklayfront.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCheckResponse {
    private String orderId;
    private Boolean valid;
    private Integer paymentAmount;
}
