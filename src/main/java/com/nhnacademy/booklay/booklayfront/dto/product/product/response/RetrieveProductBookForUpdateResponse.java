package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class RetrieveProductBookForUpdateResponse {

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
  Boolean isSelling;
  @NotNull
  Boolean pointMethod;

  @NotNull
  Long productDetailId;
  @NotNull
  String isbn;
  @NotNull
  Integer page;
  @NotNull
  String publisher;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  LocalDateTime createdAt;


  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull
  LocalDate publishedDate;
  @Setter
  String ebookAddress;
  @Setter
  Integer storage;

  List<Long> authorIds;

  List<Long> categoryIds;
}
