package com.nhnacademy.booklay.booklayfront.dto.product;

import lombok.Getter;

@Getter
public class DeleteByIdRequest {

  Long id;

  public DeleteByIdRequest(Long id) {
    this.id = id;
  }
}
