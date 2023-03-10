package com.nhnacademy.booklay.booklayfront.dto.product.tag.response;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagProductResponse {

  @NotNull
  Long id;
  @NotNull
  String name;
  @NotNull
  boolean isRegistered;
}
