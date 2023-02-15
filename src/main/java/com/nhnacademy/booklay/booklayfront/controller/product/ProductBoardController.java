package com.nhnacademy.booklay.booklayfront.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.board.response.PostResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    Integer postAskTypeNo = 2;

    PostResponse post = PostResponse.builder()
        .postTypeNo(postAskTypeNo)
        .productId(productId)
        .viewPublic(true)
        .answered(false)
        .build();


    model.addAttribute("upperPost", post);

    return "product/board/write";
  }

}
