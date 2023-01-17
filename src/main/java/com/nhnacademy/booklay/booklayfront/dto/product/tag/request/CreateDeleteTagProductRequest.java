package com.nhnacademy.booklay.booklayfront.dto.product.tag.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateDeleteTagProductRequest {

  @NotNull
  Long tagId;
  @NotNull
  Long productNo;

}
