package com.nhnacademy.booklay.booklayfront.dto.order.response;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderSheetSaveResponse {
    private Long orderId;
    private List<CartDto> cartDtoList;
    private Boolean isError;
    private String errorMessage;
}
