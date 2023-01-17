package com.nhnacademy.booklay.booklayfront.dto.product.author.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateAuthorRequest {

  @NotNull
  String name;

  Long memberNo;

  public CreateAuthorRequest(String name) {
    this.name = name;
  }
}
