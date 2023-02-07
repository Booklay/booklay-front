package com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class CreateDeleteWishlistAndAlarmRequest {
  @NotNull
  private Long memberNo;
  @NotNull
  private Long productId;
}
