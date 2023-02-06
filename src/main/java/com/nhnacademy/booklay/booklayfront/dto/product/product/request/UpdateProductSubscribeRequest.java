package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class UpdateProductSubscribeRequest {

  @NotNull
  Long productId;
  @NotNull
  String title;
  @NotNull
  Long price;
  @NotNull
  Long pointRate;
  @NotNull
  String shortDescription;
  @NotNull
  String longDescription;
  @NotNull
  Boolean selling;
  @NotNull
  Boolean pointMethod;
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @NotNull
  LocalDateTime createdAt;

  @NotNull
  List<Long> categoryIds;

  @NotNull
  Long subscribeId;
  Long subscribeWeek;
  Long subscribeDay;
  List<Long> childProducts;

  @NotNull
  String publisher;
}
