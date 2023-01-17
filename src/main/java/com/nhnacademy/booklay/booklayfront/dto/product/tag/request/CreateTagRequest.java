package com.nhnacademy.booklay.booklayfront.dto.product.tag.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateTagRequest {

  @JsonProperty
  @NotNull
  public String name;

  public CreateTagRequest(String name) {
    this.name = name;
  }
}
