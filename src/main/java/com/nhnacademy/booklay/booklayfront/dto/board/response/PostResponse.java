package com.nhnacademy.booklay.booklayfront.dto.board.response;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponse {

  private Long postNo;
  private Integer postTypeNo;
  private Long memberNo;
  private Long productNo;
  private Long groupPostNo;
  private Integer groupOrder;
  private Integer depth;
  private String title;
  private String content;
  private Boolean viewPublic;
  private Boolean answered;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private String writer;

}
