package com.nhnacademy.booklay.booklayfront.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.board.request.BoardPostCreateRequest;
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

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

  private static final String SHOP_PRE_FIX = "/shop/v1";
  private final String gatewayIp;
  private final RestService restService;
  private final ObjectMapper objectMapper = new ObjectMapper();


  @PostMapping
  public String postCreate(@Valid @ModelAttribute BoardPostCreateRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board");

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class), Void.class);

    return "redirect:/product/view/" + request.getProductNo();
  }

  @GetMapping("/{postId}")
  public String viewPost(@PathVariable Long postId, MemberInfo memberInfo, Model model) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board/" + postId);

    ApiEntity<PostResponse> response = restService.get(uri.toString(), null, PostResponse.class);

    PostResponse post = response.getBody();

    boolean commentAuth = false;
    if (memberInfo.getMemberNo() != null) {
      if (post.getMemberNo() == memberInfo.getMemberNo() || post.commentAuth(
          memberInfo.getMemberNo())) {
        commentAuth = true;
      }
    }

    model.addAttribute("post", post);
    model.addAttribute("memberInfo", memberInfo);

    model.addAttribute("commentAuth", commentAuth);
    return "board/view";
  }

  @GetMapping("/reply/{postId}")
  public String replyPost(Model model, @PathVariable Long postId, MemberInfo memberInfo){

    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board/" + postId);

    ApiEntity<PostResponse> response = restService.get(uri.toString(), null, PostResponse.class);

    PostResponse post = response.getBody();

    model.addAttribute("memberInfo", memberInfo);
    model.addAttribute("upperPost", post);
    return "product/board/write";
  }
}
