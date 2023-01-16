package com.nhnacademy.booklay.booklayfront.dto.product.tag.response;

import javax.validation.constraints.NotNull;

public class TagProductResponse {
  @NotNull
  Long id;
  @NotNull
  String name;
  @NotNull
  boolean isRegistered;
}
