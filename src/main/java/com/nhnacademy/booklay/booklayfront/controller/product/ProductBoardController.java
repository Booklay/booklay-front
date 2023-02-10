package com.nhnacademy.booklay.booklayfront.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author : 최규태
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product/board")
public class ProductBoardController {

  private static final String SHOP_PRE_FIX = "/shop/v1";
  private final String gatewayIp;
  private final RestService restService;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @GetMapping("/{productId}")
  public String productAsk(@PathVariable Long productId, MemberInfo memberInfo, Model model) {
    model.addAttribute("postTypeNo", 2);
    model.addAttribute("memberNo", memberInfo.getMemberNo());
    model.addAttribute("productId", productId);
    model.addAttribute("groupNo", 0);
    model.addAttribute("groupOrder", 0);
    model.addAttribute("depth", 0);
    model.addAttribute("answer", false);

    return "product/board/write";
  }


}
