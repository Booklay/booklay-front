package com.nhnacademy.booklay.booklayfront.dto.board.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommentRequest {

  @NotNull
  private Long postNo;
  @NotNull
  private Long memberNo;
  @NotNull
  private Long groupCommentNo;
  @NotNull
  private String content;
  @NotNull
  private Integer groupOrder;
  @NotNull
  private Integer depth;
}
