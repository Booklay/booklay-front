package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
public class RetrieveProductResponse {

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
  private Boolean selling;
  @NotNull
  private Boolean pointMethod;
  @NotNull
  private String shortDescription;
  @NotNull
  private String publisher;

  private List<RetrieveAuthorResponse> authors;

  private Boolean recommend;
}
