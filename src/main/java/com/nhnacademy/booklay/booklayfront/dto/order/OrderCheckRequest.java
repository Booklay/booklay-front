package com.nhnacademy.booklay.booklayfront.dto.order;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderCheckRequest {
    List<String> couponCodeList;
    List<CartDto> cartDtoList;
    Integer usingPoint;
    Integer paymentAmount;
}
