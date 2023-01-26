package com.nhnacademy.booklay.booklayfront.dto.category.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@RequiredArgsConstructor
public class CategoryCreateRequest {

    @NotNull
    private final Long id;

    private final Long parentCategoryId;

    @NotBlank
    @Length(max = 50)
    private final String name;

    @NotNull
    private final Boolean isExposure;

}
