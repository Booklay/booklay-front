package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateDeleteProductRecommendRequest {

  @NotNull
  Long targetId;
  @NotNull
  Long baseId;

}
