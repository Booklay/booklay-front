package com.nhnacademy.booklay.booklayfront.controller.mypage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/member/product")
@RequiredArgsConstructor
public class ProductController {

  private final RestService restService;
  private final String gatewayIp;
  private final ObjectMapper objectMapper;
  private final String SHOP_PRE_FIX = "/shop/v1";
  private final String PAGE_PRE_FIX = "member/product";

  private final Long SIZE = 20L;

  @GetMapping("/wishlist")
  public String retrieveWishlist(@RequestParam(value = "page", defaultValue = "0") int page,
      MemberInfo memberInfo, Model model) {
    Long memberId = memberInfo.getMemberNo();

    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/" + PAGE_PRE_FIX + "/wishlist?page=" + page + "&size=" + SIZE);

    ApiEntity<PageResponse<RetrieveProductResponse>> response = restService.get(uri.toString(),
        objectMapper.convertValue(memberId, MultiValueMap.class),
        new ParameterizedTypeReference<>() {
        });

    if (response.isSuccess()) {
      model.addAttribute("productList", response.getBody());
      model.addAttribute("memberId", memberId);
    }
    return null;
  }

}
