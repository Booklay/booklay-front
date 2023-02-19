package com.nhnacademy.booklay.booklayfront.dto.board.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRequest {

  private Long commentId;
  @NotNull
  private Long postId;
  @NotNull
  private Long memberNo;
  @NotNull
  private String content;
  private Long groupCommentNo;
  private Integer groupOrder;
  private Integer depth;
}
