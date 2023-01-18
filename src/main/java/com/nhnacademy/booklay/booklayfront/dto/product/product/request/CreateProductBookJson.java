package com.nhnacademy.booklay.booklayfront.dto.product.product.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CreateProductBookJson {

  @NotNull
  public String title;
  @NotNull
  public Long price;
  @NotNull
  public Long pointRate;
  @NotNull
  public String shortDescription;
  @NotNull
  public String longDescription;
  @NotNull
  public boolean isSelling;
  @NotNull
  public boolean pointMethod;

  @NotNull
  public String isbn;
  @NotNull
  public int page;
  @NotNull
  public String publisher;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull
  public LocalDate publishedDate;
  public String ebookAddress;
  public int storage;
  @NotNull
  public List<Long> authorIds;
  @NotNull
  public List<Long> categoryIds;

  public CreateProductBookJson(CreateProductBookRequest request) {
    this.title = request.getTitle();
    this.price = request.getPrice();
    this.pointRate = request.getPointRate();
    this.shortDescription = request.getShortDescription();
    this.longDescription = request.getLongDescription();
    this.isSelling = request.getIsSelling();
    this.pointMethod = request.getPointMethod();
    this.isbn = request.getIsbn();
    this.page = request.getPage();
    this.publisher = request.getPublisher();
    this.publishedDate = request.getPublishedDate();
    this.ebookAddress = request.getEbookAddress();
    this.storage = request.getStorage();
    this.authorIds = request.getAuthorIds();
    this.categoryIds = request.getCategoryIds();
  }
}
