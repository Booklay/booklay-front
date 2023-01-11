package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product/tag/maintenance")
public class AdminTagController {

  @GetMapping("/{page}")
  public String retrieveTag(@PathVariable("page") Long pageNum, Model model) {
    return "/admin/product/adminTag";
  }

  @PostMapping
  public String createTag() {

    return "redirect:";
  }

  @PutMapping
  public String updateTag() {
    return "redirect:";
  }
}
