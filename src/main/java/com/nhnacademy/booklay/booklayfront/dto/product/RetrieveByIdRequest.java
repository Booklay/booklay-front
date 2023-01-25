package com.nhnacademy.booklay.booklayfront.dto.product;

import lombok.Getter;

@Getter
public class RetrieveByIdRequest {

  Long id;

  public RetrieveByIdRequest(Long id) {
    this.id = id;
  }
}
