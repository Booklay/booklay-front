package com.nhnacademy.booklay.booklayfront.dto.board.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AnswerConfirmRequest {

  private Long postId;

  private Boolean commentAuth;
}
