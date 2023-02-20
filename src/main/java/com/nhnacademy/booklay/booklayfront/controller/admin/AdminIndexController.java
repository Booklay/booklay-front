package com.nhnacademy.booklay.booklayfront.controller.admin;

import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.board.response.PostResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminIndexController extends BaseController {

  private final String gatewayIp;
  private static final String SHOP_PRE_FIX = "/shop/v1/";
  private final RestService restService;
  private static final Integer MAIN_MAX_POST_NUM = 5;

  @GetMapping(value = {"", "/", "/profile"})
  public String getAdminPage(Model model) {

    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/board/notice/" + MAIN_MAX_POST_NUM);

    log.info(uri.toString());

    ApiEntity<List<PostResponse>> postResponse = restService.get(uri.toString(), null,
        new ParameterizedTypeReference<>() {
        });

    model.addAttribute("noticeList", postResponse.getBody());

    return "admin/main";
  }
}
