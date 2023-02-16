package com.nhnacademy.booklay.booklayfront.dto.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AnswerConfirmRequest {

  private Long postId;
  private Boolean commentAuth;
}
