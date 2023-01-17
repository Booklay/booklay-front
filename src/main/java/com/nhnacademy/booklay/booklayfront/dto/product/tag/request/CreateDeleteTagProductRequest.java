package com.nhnacademy.booklay.booklayfront.dto.product.tag.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreateDeleteTagProductRequest {

  Long tagId;
  Long productNo;

  public CreateDeleteTagProductRequest(Long tagId, Long productNo) {
    this.tagId = tagId;
    this.productNo = productNo;
  }
}
