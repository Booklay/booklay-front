package com.nhnacademy.booklay.booklayfront.dto.board.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {

  @NotNull
  private Long postId;
  @NotNull
  private Long memberNo;
  @NotNull
  private String content;
  private Long groupCommentNo;
  private Integer groupOrder;
  @NotNull
  private Integer depth;
}
