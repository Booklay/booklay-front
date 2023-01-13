package com.nhnacademy.booklay.booklayfront.coupon.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class PageResponse<T> {

    private Integer pageNumber;

    private Integer pageSize;

    private Integer totalPages; // 총 페이지 수

    private List<T> data;
}