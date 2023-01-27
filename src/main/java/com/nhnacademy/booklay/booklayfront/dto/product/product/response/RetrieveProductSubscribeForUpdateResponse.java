package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class RetrieveProductSubscribeForUpdateResponse {

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
  boolean isSelling;
  @NotNull
  boolean pointMethod;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  LocalDateTime createdAt;

  @NotNull
  List<Long> categoryIds;

  @NotNull
  Long subscribeId;
  @NotNull
  Long subscribeWeek;
  @NotNull
  Long subscribeDay;
  @NotNull
  String publisher;

  List<Long> childProducts;
}
