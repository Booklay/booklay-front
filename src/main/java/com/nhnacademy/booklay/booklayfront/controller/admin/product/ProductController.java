package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductBookRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductController {

  @GetMapping
  public String getProductMainPage() {
    return "/admin/product/productMainManage";
  }

  @GetMapping("/book/create")
  public String getProductBookForm() {
    return "/admin/product/createProductBookForm";
  }

  @PostMapping("/book/create")
  public String createProductBook(@Valid @ModelAttribute CreateProductBookRequest request) {
    log.info("제목 : "+request.getTitle());
    log.info("ISBN : "+request.getIsbn());
    log.info("긴설명 : "+request.getLongDescription());
    log.info("짧설명 : "+request.getShortDescription());
    log.info("페이지 : "+request.getPage());
    log.info("작가리스트 : "+request.getAuthorIds().get(1));

    return "redirect:/admin/product";
  }

  @GetMapping("/subscribe/create")
  public String getProductSubscribeForm() {
    return "/admin/product/createProductSubscribeForm";
  }


  @GetMapping("/author")
  public String getAuthorMaintain() {
    return "/admin/product/adminAuthor";
  }

}
