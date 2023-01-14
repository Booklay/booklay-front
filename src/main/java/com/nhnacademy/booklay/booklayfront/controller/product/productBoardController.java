package com.nhnacademy.booklay.booklayfront.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class productBoardController {

  @GetMapping("/view/{productNo}")
  public String productViewer(@PathVariable("productNo") Long productNo){
    return "/product/productDetailView";
  }
}
