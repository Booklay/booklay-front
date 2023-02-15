package com.nhnacademy.booklay.booklayfront.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDto {
    private Long productNo;
    private String productName;
    private Integer count;
    private Integer price;
}
