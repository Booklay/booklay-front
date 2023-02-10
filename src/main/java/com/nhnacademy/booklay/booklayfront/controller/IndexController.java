package com.nhnacademy.booklay.booklayfront.controller;

import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class IndexController extends BaseController {

//  private final String gatewayIp;
  private final RestService restService = new RestService(new RestTemplate());

  private static final String SHOP_PRE_FIX = "/shop/v1";

  public IndexController(RestService restService,
      String gatewayIp) {
  }

  @GetMapping(value = {"/index", "/"})
  public String home(MemberInfo memberInfo, Model model, @Value("${booklay.gateway-origin}") String ip) {
    URI uri = URI.create(ip+ SHOP_PRE_FIX + "/product/recent");

    ApiEntity<List<ProductAllInOneResponse>> productResponse = restService.get(
        uri.toString(), null, new ParameterizedTypeReference<>() {
        });

    if (Objects.nonNull(productResponse.getBody())) {
      List<ProductAllInOneResponse> productList = productResponse.getBody();
      model.addAttribute("productList", productList);
    }

    model.addAttribute("memberInfo", memberInfo);
    return "main/main";
  }

}
