package com.nhnacademy.booklay.booklayfront.controller.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.board.request.BoardPostCreateRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}
