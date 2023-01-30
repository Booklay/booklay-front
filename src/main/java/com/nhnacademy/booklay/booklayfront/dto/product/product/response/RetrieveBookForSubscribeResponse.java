package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RetrieveBookForSubscribeResponse {

  @NotNull
  Long productId;
  @NotNull
  String title;
  @NotNull
  List<String> authors;
  @NotNull
  String publisher;
  @NotNull
  Boolean isRegistered;
}
