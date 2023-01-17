package com.nhnacademy.booklay.booklayfront.dto.product.tag.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateTagRequest {

  @NotNull
  Long id;
  @NotNull
  String name;

  public UpdateTagRequest(Long id, String name) {
    this.id = id;
    this.name = name;
  }
}
