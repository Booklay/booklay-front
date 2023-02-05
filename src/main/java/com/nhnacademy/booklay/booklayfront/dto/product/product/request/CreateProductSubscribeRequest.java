package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateProductSubscribeRequest {

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
  private Boolean selling;
  @NotNull
  private Boolean pointMethod;

  @NotNull
  private List<Long> categoryIds;

  @NotNull
  private Integer subscribeWeek;

  @NotNull
  private Integer subscribeDay;

  @NotNull
  private String publisher;

  private List<Long> childProducts;
}
