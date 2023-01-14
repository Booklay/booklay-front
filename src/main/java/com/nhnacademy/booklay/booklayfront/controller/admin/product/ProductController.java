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
  public String getProductMainPage(){
    return "/admin/product/productMainManage";
  }

  @GetMapping("/book/create")
  public String getProductBookForm(){
    return "/admin/product/createProductBookForm";
  }

  @GetMapping("/subscribe/create")
  public String getProductSubscribeForm(){
    return "/admin/product/createProductSubscribeForm";
  }


  @GetMapping("/author")
  public String getAuthorMaintain(){
    return "/admin/product/adminAuthor";
  }

}
