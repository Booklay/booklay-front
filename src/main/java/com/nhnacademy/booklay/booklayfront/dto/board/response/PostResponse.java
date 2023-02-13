package com.nhnacademy.booklay.booklayfront.dto.board.response;

import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostResponse {

  private Long postId;
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

  private List<RetrieveAuthorResponse> authorList;

  public boolean commentAuth(Long memberNo) {
    for (RetrieveAuthorResponse author : authorList) {
      if (author.getMember() != null) {
        if (author.getMember().getMemberNo() == memberNo) {
          return true;
        }
      }
    }
    return false;
  }

}
