package com.nhnacademy.booklay.booklayfront.dto.product.tag.response;

import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RetrieveTagResponse {

  @NotNull
  Long id;
  @NotNull
  String name;
}