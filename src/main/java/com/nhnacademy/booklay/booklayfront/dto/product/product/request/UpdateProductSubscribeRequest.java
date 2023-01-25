package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Setter
public class UpdateProductSubscribeRequest {

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
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  @NotNull
  private LocalDateTime registedAt;

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
