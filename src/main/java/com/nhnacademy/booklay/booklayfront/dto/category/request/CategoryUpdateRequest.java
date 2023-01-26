package com.nhnacademy.booklay.booklayfront.dto.category.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
public class CategoryUpdateRequest {

    @NotNull
    private Long id;

    private Long parentCategoryId;

    @NotBlank
    @Length(max = 50)
    private String name;

    @NotNull
    private Boolean isExposure;

}
