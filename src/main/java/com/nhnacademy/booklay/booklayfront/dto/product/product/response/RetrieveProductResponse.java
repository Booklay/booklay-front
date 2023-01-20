package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveProductResponse {

  @NotNull
  Long productId;
  @Setter
  MultipartFile image;
  @NotNull
  String title;
  @NotNull
  Long price;
  @NotNull
  Long pointRate;
  @NotNull
  boolean isSelling;
  @NotNull
  boolean pointMethod;
  @NotNull
  String shortDescription;
  @NotNull
  String publisher;

  List<RetrieveAuthorResponse> authors;
}
