package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class UpdateProductBookRequest {

  @Getter
  @NotNull
  private Long productId;
  @NotNull
  private String title;
  private Long originalImage;
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
  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @NotNull
  private LocalDateTime createdAt;


  @NotNull
  private Long productDetailId;
  @NotNull
  private String isbn;
  @NotNull
  private Integer page;
  @NotNull
  private String publisher;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull
  private LocalDate publishedDate;
  @Setter
  private String ebookAddress;
  @Setter
  private Integer storage;
  @NotNull
  private List<Long> authorIds;
  @NotNull
  private List<Long> categoryIds;
}
