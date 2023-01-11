package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.UpdateTagRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/product/tag/maintenance")
public class AdminTagController {

  @GetMapping("/{page}")
  public String retrieveTag(@PathVariable("page") Long pageNum, Model model) {
    if (pageNum < 0) {
      pageNum = 0L;
    }

    return "/admin/product/adminTag";
  }

  @PostMapping
  public String createTag(@Valid @ModelAttribute CreateTagRequest request) {

    return "redirect:";
  }

  @PutMapping
  public String updateTag(@Valid @ModelAttribute UpdateTagRequest request) {

    return "redirect:";
  }
}
