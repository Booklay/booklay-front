package com.nhnacademy.booklay.booklayfront.dto.category.response;


import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 단계형 카테고리 메뉴 DTO.
 */
@Getter
@NoArgsConstructor
public class CategorySteps {

    Long id;
    String name;
    List<CategorySteps> categories;
}
