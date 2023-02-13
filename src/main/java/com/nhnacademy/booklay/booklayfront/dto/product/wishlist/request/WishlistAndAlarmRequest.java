package com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WishlistAndAlarmRequest {
  @NotNull
  private Long memberNo;
  @NotNull
  private Long productId;
}
