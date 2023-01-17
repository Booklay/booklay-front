package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

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
  private boolean isSelling;
  @NotNull
  private boolean pointMethod;

  @NotNull
  private String isbn;
  @NotNull
  private int page;
  @NotNull
  private String publisher;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull
  private LocalDate publishedDate;
  private String ebookAddress;
  private int storage;
  @NotNull
  private List<Long> authorIds;
  @NotNull
  private List<Long> categoryIds;

  public boolean getIsSelling(){
    return this.isSelling;
  }

  public boolean getPointMethod(){
    return this.pointMethod;
  }

  public CreateProductBookRequest(String title, Long price,
      Long pointRate, String shortDescription, String longDescription, boolean isSelling,
      boolean pointMethod, String isbn, int page, String publisher,
      LocalDate publishedDate, List<Long> authorIds,
      List<Long> categoryIds) {
    this.title = title;
    this.price = price;
    this.pointRate = pointRate;
    this.shortDescription = shortDescription;
    this.longDescription = longDescription;
    this.isSelling = isSelling;
    this.pointMethod = pointMethod;
    this.isbn = isbn;
    this.page = page;
    this.publisher = publisher;
    this.publishedDate = publishedDate;
    this.authorIds = authorIds;
    this.categoryIds = categoryIds;
  }
}
