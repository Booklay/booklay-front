package com.nhnacademy.booklay.booklayfront.controller.board;

import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getMemberInfoMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.board.request.CommentChangeRequest;
import com.nhnacademy.booklay.booklayfront.dto.board.request.CommentRequest;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

  private static final String SHOP_PRE_FIX = "/shop/v1";
  private final String gatewayIp;
  private final RestService restService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @PostMapping()
  public String createComment(@Valid @ModelAttribute CommentRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/comment");

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class), Long.class);

    return "redirect:/board/" + request.getPostId();
  }

  @PutMapping()
  public String updateComment(@Valid @ModelAttribute CommentRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/comment");

    restService.put(uri.toString(), objectMapper.convertValue(request, Map.class), Long.class);
    return "redirect:/board/" + request.getPostId();
  }

  @DeleteMapping()
  public String deleteComment(@Valid @ModelAttribute CommentChangeRequest request, MemberInfo memberInfo) {
    if(request.getMemberNo().equals(memberInfo.getMemberNo())) {
      URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/comment/" + request.getCommentId());

      MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
      map.setAll(getMemberInfoMap(memberInfo));

      restService.delete(uri.toString(), map);

      return "redirect:/board/" + request.getPostId();
    }
    return "/error";
  }
}
