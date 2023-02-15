package com.nhnacademy.booklay.booklayfront.dto.review.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewDeleteRequest {

    private Long reviewId;
    private Long writerNo;

}
