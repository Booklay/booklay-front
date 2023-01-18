package com.nhnacademy.booklay.booklayfront.dto.product.tag.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateDeleteTagProductRequest {

  @NotNull
  Long tagId;
  @NotNull
  Long productNo;

  public CreateDeleteTagProductRequest(Long tagId, Long productNo) {
    this.tagId = tagId;
    this.productNo = productNo;
  }
}
