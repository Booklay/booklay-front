package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class RetrieveProductBookForUpdateResponse {

  private Long productId;

  private String title;
  private Long objectFileId;
  private Long price;
  private Long pointRate;
  private String shortDescription;
  private String longDescription;
  private Boolean selling;
  private Boolean pointMethod;

  private Long productDetailId;
  private String isbn;
  private Integer page;
  private String publisher;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  private LocalDateTime createdAt;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate publishedDate;
  @Setter
  private String ebookAddress;
  private Integer storage;

  @NotNull
  private List<RetrieveAuthorResponse> authorIds;

  @NotNull
  private List<CategoryResponse> categoryList;

  @NotNull
  private List<RetrieveTagResponse> tagList;
}
