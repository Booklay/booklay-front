package com.nhnacademy.booklay.booklayfront.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.board.request.BoardPostUpdateRequest;
import com.nhnacademy.booklay.booklayfront.dto.board.response.PostResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author : 최규태
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

  private static final String SHOP_PRE_FIX = "/shop/v1";
  private final String gatewayIp;
  private final RestService restService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 게시글 생성
   * @param request
   * @return
   */
  @PostMapping
  public String postCreate(@Valid @ModelAttribute BoardPostCreateRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board");

    ApiEntity<Long> result = restService.post(uri.toString(), objectMapper.convertValue(request, Map.class), Long.class);

    return "redirect:/board/" + result.getBody();
  }

  /**
   * 게시글 조회
   * @param postId
   * @param memberInfo
   * @param model
   * @return
   */
  @GetMapping("/{postId}")
  public String viewPost(@PathVariable Long postId, MemberInfo memberInfo, Model model) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board/" + postId);

    ApiEntity<PostResponse> response = restService.get(uri.toString(), null, PostResponse.class);

    PostResponse post = response.getBody();

    boolean commentAuth = commentAuthCheck(memberInfo, post);

    model.addAttribute("post", post);
    model.addAttribute("memberInfo", memberInfo);

    model.addAttribute("commentAuth", commentAuth);
    return "board/view";
  }

  /**
   * 상품 QNA 게시글 등록 화면 호출
   * @param productId
   * @param memberInfo
   * @param model
   * @return
   */
  @GetMapping("/productQNA/{productId}")
  public String productAsk(@PathVariable Long productId, MemberInfo memberInfo, Model model) {
    Integer postAskTypeNo = 2;

    PostResponse post = PostResponse.builder()
        .postTypeNo(postAskTypeNo)
        .productId(productId)
        .viewPublic(true)
        .answered(false)
        .build();

    model.addAttribute("upperPost", post);

    return "board/write";
  }

  /**
   * 답글 등록 화면 호출
   * @param model
   * @param postId
   * @param memberInfo
   * @return
   */
  @GetMapping("/reply/{postId}")
  public String replyPost(Model model, @PathVariable Long postId, MemberInfo memberInfo) {

    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board/" + postId);

    ApiEntity<PostResponse> response = restService.get(uri.toString(), null, PostResponse.class);

    PostResponse post = response.getBody();

    model.addAttribute("memberInfo", memberInfo);
    model.addAttribute("upperPost", post);
    return "board/write";
  }

  /**
   * 게시글 수정 화면 호출
   * @param model
   * @param postId
   * @param memberInfo
   * @return
   */
  @GetMapping("/edit/{postId}")
  public String editPost(Model model, @PathVariable Long postId, MemberInfo memberInfo) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board/" + postId);

    ApiEntity<PostResponse> response = restService.get(uri.toString(), null, PostResponse.class);

    PostResponse post = response.getBody();

    boolean commentAuth = commentAuthCheck(memberInfo, post);

    model.addAttribute("memberInfo", memberInfo);
    model.addAttribute("editPost", post);
    model.addAttribute("commentAuth", commentAuth);
    return "board/edit";
  }

  /**
   * 게시글 수정 요청
   * @param postId
   * @param request
   * @return
   */

  @PostMapping("/edit/{postId}")
  public String editPost(@PathVariable Long postId, @Valid @ModelAttribute BoardPostUpdateRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board");

    restService.put(uri.toString(), objectMapper.convertValue(request, Map.class), Void.class);

    return "redirect:/board/" + postId;
  }

  /**
   * 게시글 조회 권한 확인
   * @param memberInfo
   * @param post
   * @return
   */

  private boolean commentAuthCheck(MemberInfo memberInfo, PostResponse post) {
    boolean commentAuth = false;
    if (memberInfo.getMemberNo() != null) {
      if (post.getMemberNo() == memberInfo.getMemberNo() || post.commentAuth(
          memberInfo.getMemberNo())) {
        commentAuth = true;
      }
    }
    return commentAuth;
  }
}
