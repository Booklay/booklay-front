package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class CreateProductBookRequest {

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
