package com.nhnacademy.booklay.booklayfront.dto.order.response;

import com.nhnacademy.booklay.booklayfront.dto.order.OrderSheet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderSheetCheckResponse extends OrderSheet {
    private String reason;
    private Boolean success;
}
