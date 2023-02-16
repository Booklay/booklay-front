package com.nhnacademy.booklay.booklayfront.dto.board.response;


import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CommentResponse {

  private Long commentId;
  private Long memberNo;
  private String name;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long groupCommentNo;
  private Long groupOrder;
  private Integer depth;

}