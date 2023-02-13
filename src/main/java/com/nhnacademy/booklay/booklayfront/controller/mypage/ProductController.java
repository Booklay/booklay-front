package com.nhnacademy.booklay.booklayfront.controller.mypage;

import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : 최규태
 */
@Slf4j
@Controller
@RequestMapping("/member/product")
@RequiredArgsConstructor
public class ProductController {

  private final RestService restService;
  private final String gatewayIp;
  private final ObjectMapper objectMapper;
  private final String SHOP_PRE_FIX = "/shop/v1";
  private static final String PAGE_PRE_FIX = "member/product";

  private static final String REDIRECT_WISH = "redirect:/member/product/wishlist";
  private static final String REDIRECT_ALARM = "redirect:/member/product/alarm";
  private static final Integer SIZE = 20;

  /**
   * 마이페이지에서 위시리스트 목록 조회
   * @param page
   * @param memberInfo
   * @param model
   * @return
   */
  @GetMapping("/wishlist")
  public String retrieveWishlist(@RequestParam(value = "page", defaultValue = "0") int page,
      MemberInfo memberInfo, Model model) {
    Long memberId = memberInfo.getMemberNo();

    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/mypage/product/wishlist/" + memberId);

    ApiEntity<PageResponse<RetrieveProductResponse>> response = restService.get(uri.toString(),
        getDefaultPageMap(page, SIZE), new ParameterizedTypeReference<>() {
        });
    if (response.isSuccess()) {
      setCurrentPageAndMaxPageToModel(model, response.getBody());
      model.addAttribute("productList", response.getBody().getData());
      model.addAttribute("memberId", memberId);
      return "mypage/product/wishlist";
    }
    return "mypage/profile/main";
  }

  /**
   * 위시리스트에서 위시리스트 등록 해제
   * @param request
   * @return
   */
  @PostMapping("/wishlist/disconnect")
  public String deleteWishlistFromMyPage(WishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/wishlist/");

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));

    return REDIRECT_WISH;
  }


  /**
   * 위시리스트에서 재입고 알림 등록
   * @param request
   * @return
   */
  @PostMapping("/wishlist/alarm/connect")
  public String alarmConnectFromWish(WishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/alarm/");

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
        WishlistAndAlarmRequest.class);

    return REDIRECT_WISH;
  }

  /**
   * 위시리스트에서 재입고 알림 취소
   * @param request
   * @return
   */
  @PostMapping("/wishlist/alarm/disconnect")
  public String alarmDisconnectFromWish(WishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/alarm/");

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));

    return REDIRECT_WISH;
  }

  /**
   * 재입고 알림 목록 조회
   * @param page
   * @param memberInfo
   * @param model
   * @return
   */
  @GetMapping("/alarm")
  public String retrieveAlarmList(@RequestParam(value = "page", defaultValue = "0") int page,
      MemberInfo memberInfo, Model model) {
    Long memberId = memberInfo.getMemberNo();

    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/mypage/product/alarm/" + memberId);

    ApiEntity<PageResponse<RetrieveProductResponse>> response = restService.get(uri.toString(),
        getDefaultPageMap(page, SIZE), new ParameterizedTypeReference<>() {
        });
    if (response.isSuccess()) {
      setCurrentPageAndMaxPageToModel(model, response.getBody());
      model.addAttribute("productList", response.getBody().getData());
      model.addAttribute("memberId", memberId);
      return "mypage/product/alarm";
    }
    return "mypage/profile/main";
  }

  /**
   * 재입고 알림 목록에서 등록 취소
   * @param request
   * @return
   */
  @PostMapping("/alarm/disconnect")
  public String alarmDisconnect(WishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/alarm/");

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));

    return REDIRECT_ALARM;
  }
}
