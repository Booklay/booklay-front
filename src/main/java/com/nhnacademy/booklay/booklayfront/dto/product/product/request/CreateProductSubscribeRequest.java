package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
  private boolean isSelling;
  @NotNull
  private boolean pointMethod;

  @NotNull
  private List<Long> categoryIds;

  @NotNull
  @Length(max=4)
  private Integer subscribeWeek;

  @NotNull
  @Length(max=6)
  private Integer subscribeDay;

  @NotNull
  private String publisher;
}
