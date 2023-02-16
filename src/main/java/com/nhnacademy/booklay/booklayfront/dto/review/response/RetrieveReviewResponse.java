package com.nhnacademy.booklay.booklayfront.dto.review.response;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RetrieveReviewResponse {

    Long id;
    String content;
    Long score;
    Long productId;
    String writerName;
    Long writerNo;
    Long imageNo;

}
