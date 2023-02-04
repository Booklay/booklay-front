package com.nhnacademy.booklay.booklayfront.dto.search.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchRequest {

    @NotBlank
    String keywords;

    @NotBlank
    String classification;

}
