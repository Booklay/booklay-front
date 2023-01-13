package com.nhnacademy.booklay.booklayfront.dto.product.tag.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateTagRequest {
  @NotNull
  Long id;
  @NotNull
  String name;
}
