package com.nhnacademy.booklay.booklayfront.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderProductDto {
    private Long productNo;
    private Integer count;
    private Long price;
}
