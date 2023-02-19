package com.nhnacademy.booklay.booklayfront.dto.board.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardPostCreateRequest {

  @NotNull
  private Integer postTypeNo;
  @NotNull
  private Long memberNo;
  private Long productNo;
  private Long groupNo;
  private Integer groupOrderNo;
  private Integer depth;
  @NotNull
  private String title;
  @NotNull
  private String content;
  @NotNull
  private Boolean viewPublic;
  private Boolean answered;
}
