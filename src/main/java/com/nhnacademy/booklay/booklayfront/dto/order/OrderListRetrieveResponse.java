package com.nhnacademy.booklay.booklayfront.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderListRetrieveResponse {
    private Long orderNo;
    private Long orderStatusCodeNo;
    private String orderStatusName;
    private Long productPriceSum;
    private Long paymentPrice;

    private String orderTitle;
    private Integer pointAccumulate;

}
