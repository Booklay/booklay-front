package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.booklayfront.service.IndexService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController extends BaseController {
  private final IndexService indexService;

  @GetMapping(value = {"/index", "/",""})
  public String home(MemberInfo memberInfo, Model model) {

    List<SearchProductResponse> productResponses = indexService.getRecommendProducts();

    model.addAttribute("productList", productResponses);
    model.addAttribute("memberInfo", memberInfo);

    return "main/main";
  }

}
