package com.nhnacademy.booklay.booklayfront.dto.product.product.response;

import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class RetrieveProductSubscribeForUpdateResponse {

  @NotNull
  Long productId;
  @NotNull
  String title;

  @NotNull
  Long objectFileId;

  @NotNull
  Long price;
  @NotNull
  Long pointRate;
  @NotNull
  String shortDescription;
  @NotNull
  String longDescription;
  @NotNull
  Boolean selling;
  @NotNull
  Boolean pointMethod;
  @DateTimeFormat(pattern = "yyyy-MM-dd hh:MM:ss")
  LocalDateTime createdAt;

  @NotNull
  Long subscribeId;
  @NotNull
  Long subscribeWeek;
  @NotNull
  Long subscribeDay;
  @NotNull
  String publisher;

  @NotNull
  List<CategoryResponse> categoryList;

  @NotNull
  List<RetrieveTagResponse> tagList;
}
