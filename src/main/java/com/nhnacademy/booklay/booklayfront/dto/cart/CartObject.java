package com.nhnacademy.booklay.booklayfront.dto.cart;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CartObject {
    private Long productNo;
    private String productName;
    private Long productPrice;
    @Setter
    private Integer productCount;
    private List<Long> categoryNoList;
    private Long imageNo;
}
