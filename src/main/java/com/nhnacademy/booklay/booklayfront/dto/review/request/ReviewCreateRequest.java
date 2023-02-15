package com.nhnacademy.booklay.booklayfront.dto.review.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ReviewCreateRequest {

    @Setter
    private Long productId;
    private Long score;
    private String content;
    private Long memberNo;

}
