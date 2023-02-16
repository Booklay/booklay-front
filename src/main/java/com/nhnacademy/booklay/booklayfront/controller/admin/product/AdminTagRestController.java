package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product/tag")
public class AdminTagRestController {

  private static final String SHOP_URI_PRE_FIX = "/shop/v1/admin/tag";
  private final String gatewayIp;
  private final ObjectMapper objectMapper;
  private final RestService restService;

  /**
   * 태그 증복검사를 위한 호출
   *
   * @return
   */
  @PostMapping("/{name}")
  public Boolean retrieveAllTag(@PathVariable String name) {
    if (name != null) {
      URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX + "/exist/" + name);

      ApiEntity<Boolean> result = restService.get(uri.toString(), null
          , Boolean.class);

      log.info("결과 출력 " + result.getBody());
      return result.getBody();
    }
    return false;
  }
}
