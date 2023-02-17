package com.nhnacademy.booklay.booklayfront.dto.order;

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
