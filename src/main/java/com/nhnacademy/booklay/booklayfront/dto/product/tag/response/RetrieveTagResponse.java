package com.nhnacademy.booklay.booklayfront.dto.product.tag.response;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
public class RetrieveTagResponse {
  @NotNull
  Long id;
  @NotNull
  String name;
}