package com.nhnacademy.booklay.booklayfront.dto.category.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * javadoc.
 * RestTemplate Json 을 반환 받는 DTO
 */
@Getter
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String name;
    private Long parentCategoryId;
    private Long depth;
    private Boolean isExposure;
}
