package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import java.time.LocalDate;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ProductDetailResponse {

    private Long id;
    private String isbn;
    private Integer page;
    private String publisher;
    private LocalDate publishedDate;
    private String ebookAddress;
    private Integer storage;

}
