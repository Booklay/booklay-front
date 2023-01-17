package com.nhnacademy.booklay.booklayfront.dto.product.tag.response;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagProductResponse {

  @NotNull
  Long id;
  @NotNull
  String name;
  @NotNull
  boolean isRegistered;
}
