package com.nhnacademy.booklay.booklayfront.dto.search.response;


import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class SearchProductResponse {
    private Long id;
    private Long thumbnail;
    private String title;
    private String publisher;
    private Long price;
    private Boolean selling;
    private List<AuthorInfo> authors = new ArrayList<>();

}
