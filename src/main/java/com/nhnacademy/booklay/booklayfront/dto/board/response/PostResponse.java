package com.nhnacademy.booklay.booklayfront.dto.board.response;

import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

  @Setter
  private Long postId;
  private Integer postTypeNo;
  private Long memberNo;
  private Long productId;
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
  private Boolean deleted;
  private List<RetrieveAuthorResponse> authorList;

  //권한 확인
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

  //게시글 작성 시 요구조건 채워넣는거
  @Builder
  public PostResponse(Integer postTypeNo, Long memberNo, Long productId,
      Long groupPostNo, Integer groupOrder, Integer depth, String title, String content,
      Boolean viewPublic, Boolean answered, LocalDateTime createdAt, LocalDateTime updatedAt,
      String writer, List<RetrieveAuthorResponse> authorList) {
    this.postTypeNo = postTypeNo;
    this.memberNo = memberNo;
    this.productId = productId;
    this.groupPostNo = groupPostNo;
    this.groupOrder = groupOrder;
    this.depth = depth;
    this.title = title;
    this.content = content;
    this.viewPublic = viewPublic;
    this.answered = answered;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.writer = writer;
    this.authorList = authorList;
  }
}
