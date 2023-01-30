package com.nhnacademy.booklay.booklayfront.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartObject {
    private Long productNo;
    private String productName;
    private Long productPrice;
    private Integer productCount;
}
