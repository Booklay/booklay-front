package com.nhnacademy.booklay.booklayfront.dto.search.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SearchIdRequest {

    private String classification;
    private Long id;
}
