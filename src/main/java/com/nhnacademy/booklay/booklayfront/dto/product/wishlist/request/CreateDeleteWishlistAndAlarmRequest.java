package com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeleteWishlistAndAlarmRequest {
  @NotNull
  Long memberNo;
  @NotNull
  Long productId;
}
