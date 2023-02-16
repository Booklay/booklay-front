package com.nhnacademy.booklay.booklayfront.dto.board.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BoardPostDeleteRequest {
  @NotNull
  private Long postId;
  @NotNull
  private Long memberNo;
  private Long productId;
  private Integer postTypeNo;
}

// 나중에 돌려쓸거
//<form method="get" th:action="@{/board/delete/{postId} (postId = ${post.postId})}">
//<input type="hidden" name="_method" value="DELETE"/>
//<input name="memberNo" type="number" hidden="hidden" th:value="${post.memberNo}">
//<input th:if="${post.productId != null}" name="productId" type="number" hidden="hidden" th:value="${post.productId}">
//<input name="postTypeNo" type="number" hidden="hidden" th:value="${post.postTypeNo}">
//<input class="btn btn-outline-danger" type="submit" value="삭제">
//</form>