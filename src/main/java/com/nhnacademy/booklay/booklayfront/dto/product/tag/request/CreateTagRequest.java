package com.nhnacademy.booklay.booklayfront.dto.product.tag.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateTagRequest {

  @JsonProperty
  @NotNull
  public String name;
}
