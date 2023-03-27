package com.nhnacademy.booklay.booklayfront.dto.order.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderCheckResponse {
    private String orderId;
    private Boolean valid;
    private Integer paymentAmount;
    private String reason;
    private Integer reasonType;
}
