package com.nhnacademy.booklay.booklayfront.dto.product.tag.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RetrieveTagResponse {
  @NotNull
  Long id;
  @NotNull
  String name;
}