package com.nhnacademy.booklay.booklayfront.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request.CreateWishlistRequest;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class productBoardController {

  private final String gatewayIp;
  private final RestTemplate restTemplate;
  private final ObjectMapper mapper = new ObjectMapper();

  @GetMapping("/view/{productNo}")
  public String productViewer(@PathVariable("productNo") Long productNo, Model model) {

    model.addAttribute("productNo", productNo);

    return "/product/productDetailView";
  }

  @PostMapping("/view/wishlist/register")
  public String wishlistRegister(@Valid @ModelAttribute CreateWishlistRequest request)
      throws JsonProcessingException {

    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/wishlist/");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.POST, uri);

    restTemplate.exchange(requestEntity, String.class);

    return "redirect:/product/view/" + request.getProductNo();
  }
}
