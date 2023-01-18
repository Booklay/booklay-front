package com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateWishlistRequest {
  @NotNull
  Long productNo;
  @NotNull
  Long memberNo;
}
