package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductController {

  @GetMapping
  public String getProductMaintain(){
    return "/admin/product/productMainManage";
  }

  @GetMapping("/author")
  public String getAuthorMaintain(){
    return "/admin/product/adminAuthor";
  }

}
