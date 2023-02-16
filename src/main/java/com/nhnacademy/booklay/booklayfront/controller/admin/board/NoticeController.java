package com.nhnacademy.booklay.booklayfront.controller.admin.board;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.board.response.PostResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/admin/board/notice")
@RequiredArgsConstructor
public class NoticeController {

  private final String gatewayIp;
  private final ObjectMapper objectMapper;
  private final RestService restService;

  @GetMapping
  public String viewWritePage(Model model, MemberInfo memberInfo){
    Integer postAskTypeNo = 5;

    PostResponse post = PostResponse.builder()
        .postTypeNo(postAskTypeNo)
        .viewPublic(true)
        .build();

    model.addAttribute("upperPost", post);
    model.addAttribute("memberInfo", memberInfo);
    return "board/write";
  }
}
