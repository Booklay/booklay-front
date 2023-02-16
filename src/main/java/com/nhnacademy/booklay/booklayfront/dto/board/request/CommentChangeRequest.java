package com.nhnacademy.booklay.booklayfront.dto.board.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentChangeRequest {
  @NotNull
  private Long postId;
  @NotNull
  private Long commentId;
  @NotNull
  private Long memberNo;

}
