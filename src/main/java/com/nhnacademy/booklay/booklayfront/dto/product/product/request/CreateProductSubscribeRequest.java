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

  //TODO : 삭제 예정
  private Integer subscribeWeek;
  private Integer subscribeDay;
  private List<Long> childProducts;

  @NotNull
  private String publisher;
}
