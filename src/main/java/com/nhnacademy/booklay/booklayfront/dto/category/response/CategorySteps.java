package com.nhnacademy.booklay.booklayfront.dto.category.response;


import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 단계형 카테고리 메뉴 DTO.
 */
@Getter
@NoArgsConstructor
@ToString
public class CategorySteps {

    private Long id;
    private String name;
    private Long parentId;
    private List<CategorySteps> categories;
}
