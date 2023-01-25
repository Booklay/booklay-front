package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import java.util.List;
import javax.validation.constraints.NotNull;

public class RetrieveProductSubscribeForUpdateResponse {

  @NotNull
  private Long productId;
  @NotNull
  private String title;
  @NotNull
  private Long price;
  @NotNull
  private Long pointRate;
  @NotNull
  private String shortDescription;
  @NotNull
  private String longDescription;
  @NotNull
  private boolean isSelling;
  @NotNull
  private boolean pointMethod;

  @NotNull
  private List<Long> categoryIds;

  @NotNull
  private Long subscribeId;

  @NotNull
  private Long subscribeWeek;

  @NotNull
  private Long subscribeDay;

  @NotNull
  private String publisher;

  private List<Long> childProducts;
}
