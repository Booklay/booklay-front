package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RetrieveProductViewResponse {

  @NotNull
  private Long productId;
  @Setter
  private Long objectFileId;
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
  private Boolean isSelling;
  @NotNull
  private Boolean pointMethod;

  private List<RetrieveTagResponse> productTags;

  //책 상세
  private Long productDetailId;
  private String isbn;
  private Integer page;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate publishedDate;
  @Setter
  private String ebookAddress;
  @Setter
  private Integer storage;
  private List<RetrieveAuthorResponse> authors;
  private List<Long> categoryIds;

  //구독 상품 상세
  private Long subscribeId;
  @Length(max = 4)
  private Long subscribeWeek;
  @Length(max = 7)
  private Long subscribeDay;
  private List<Long> childProducts;

  //책 구독 상품 상세 공통
  @NotNull
  private String publisher;
}


