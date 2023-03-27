package com.nhnacademy.booklay.booklayfront.dto.order.request;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StorageRequest {
    private List<CartDto> cartDtoList;

}
