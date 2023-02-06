package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SubscribeResponse {
    private Long id;

    private int subscribeWeek;

    private int subscribeDay;

    private String publisher;
}