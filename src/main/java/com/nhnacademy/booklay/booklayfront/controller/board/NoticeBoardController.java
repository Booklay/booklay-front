package com.nhnacademy.booklay.booklayfront.controller.board;

import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.board.response.PostResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author : 최규태
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board/notice")
public class NoticeBoardController {

  private final String gatewayIp;
  private final ObjectMapper objectMapper;
  private final RestService restService;
  private static final String SHOP_PRE_FIX = "/shop/v1";
  private static final Integer POST_TYPE_NOTICE = 5;
  private static final Integer SIZE = 20;

  @GetMapping
  public String viewNoticeBoard(Model model,
      @RequestParam(value = "page", defaultValue = "0") int page) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board/notice");

    ApiEntity<PageResponse<PostResponse>> postResponse = restService.get(uri.toString(),
        getDefaultPageMap(page, SIZE), new ParameterizedTypeReference<>() {
        });

    model.addAttribute("postList", postResponse.getBody().getData());
    setCurrentPageAndMaxPageToModel(model, postResponse.getBody());

    return "board/notice";
  }
}
