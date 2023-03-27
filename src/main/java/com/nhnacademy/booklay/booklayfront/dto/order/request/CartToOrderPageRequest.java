package com.nhnacademy.booklay.booklayfront.dto.order.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartToOrderPageRequest {
    private List<Long> productNo;
    private List<Integer> count;

    public CartToOrderPageRequest() {
        this.productNo = new ArrayList<>();
        this.count = new ArrayList<>();
    }
}
