package com.nhnacademy.booklay.booklayfront.dto.search.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchKeywordsRequest {

    @NotBlank
    String classification;

    @NotBlank
    String keywords;
}
