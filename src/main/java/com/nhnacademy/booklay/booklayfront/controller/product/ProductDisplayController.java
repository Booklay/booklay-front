package com.nhnacademy.booklay.booklayfront.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategorySteps;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductViewResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.category.CategoryService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * @author 최규태
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductDisplayController extends BaseController {

  private static final String SHOP_PRE_FIX = "/shop/v1/product";
  private final String gatewayIp;
  private final RestTemplate restTemplate;
  private final RestService restService;
  private final ObjectMapper mapper = new ObjectMapper();

  private final CategoryService categoryService;

  //게시판형 전채 상품 호출
  @GetMapping("/display")
  public String retrieveProduct(
      Model model,
      @RequestParam(value = "page", required = false) Optional<Integer> pageNum,
      @RequestParam(value="CID", required = false) Long cid
  ) {

    List<CategorySteps> categorySteps = (List<CategorySteps>) model.getAttribute("categories");

    if(Objects.isNull(cid)){
      cid = categoryService.getDefaultCategoryId(categorySteps);
    }

    CategorySteps currentCategory = categoryService.getCurrentCategory(categorySteps,cid);

    log.error(" currentCategory : {}", currentCategory);

    if (pageNum.isEmpty()) {
      pageNum = Optional.of(0);
    }
    if (pageNum.get() < 0) {
      pageNum = Optional.of(0);
    }
    Long size = 20L;

    pageNum = Optional.of(pageNum.get() - 1);

    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX + "?page=" + pageNum.get() + "&size=" + size);

    ApiEntity<PageResponse<RetrieveProductResponse>> productResponse = restService.get(
        uri.toString(), null, new ParameterizedTypeReference<>() {
        });

    if (Objects.nonNull(productResponse.getBody())) {
      int totalPage = productResponse.getBody().getTotalPages();
      int currentPage = productResponse.getBody().getPageNumber();
      List<RetrieveProductResponse> productList = productResponse.getBody().getData();

      model.addAttribute("currentPage", currentPage);
      model.addAttribute("totalPage", totalPage);
      model.addAttribute("productList", productList);
      model.addAttribute("currentCategory", currentCategory);
    }

    return "product/display";
  }

  //상품 상세 보기
  @GetMapping("/view/{productNo}")
  public String productViewer(@PathVariable("productNo") Long productNo, Model model) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    //최초 상품 상세 정보 호출
    URI mainUri = URI.create(gatewayIp + SHOP_PRE_FIX + "/view/" + productNo);

    ApiEntity<RetrieveProductViewResponse> response = restService.get(mainUri.toString(), null,
        RetrieveProductViewResponse.class);

    model.addAttribute("productNo", productNo);
    model.addAttribute("product", response.getBody());

    //연관 상품 목록 호출
    URI recommendUri = URI.create(gatewayIp + SHOP_PRE_FIX + "/recommend/" + productNo);

    ApiEntity<List<RetrieveProductResponse>> recommendGoodsResponse = restService.get(
        recommendUri.toString(), null, new ParameterizedTypeReference<>() {
        });

    model.addAttribute("recommendProducts", recommendGoodsResponse.getBody());

    //구독 상품의 경우 구독의 자식 상품들 목록 호출
    if (Objects.nonNull(response.getBody().getSubscribeId())) {
      Long subscribeId = response.getBody().getSubscribeId();

      URI uriForSubscribe = URI.create(gatewayIp + SHOP_PRE_FIX + "/view/subscribe/" + subscribeId);

      ApiEntity<List<RetrieveProductResponse>> subscribeResponse = restService.get(
          uriForSubscribe.toString(), null, new ParameterizedTypeReference<>() {
          });

      model.addAttribute("booksAtSubscribe", subscribeResponse.getBody());
    }

    return "product/view";
  }


  //  @GetMapping("/display")
  public String categoryViewer(
      @RequestParam(name = "CID", defaultValue = "1") List<Long> categoryId,
      @RequestParam(name = "page", defaultValue = "0") Long pageNum,
      Model model) {

//        TODO SHOP 컨트롤러 매핑 후 주석 해제

        /*
                  ApiEntity<PageResponse<RetrieveProductResponse>> response =
                      restService.get(gatewayIp + SHOP_PRE_FIX + "/categories?CID=" + categoryId.toString() + "&page=" + pageNum, null, new ParameterizedTypeReference<>() {});
          <p>
                  PageResponse<RetrieveProductResponse> responseBody = response.getBody();
          <p>
                  model.addAttribute("currentPage", responseBody.getPageNumber());
                  model.addAttribute("totalPage", responseBody.getTotalPages());
                  model.addAttribute("productList", responseBody.getData());
         */

    return "redirect:/product/board";
  }

  //찜(위시리스트) 등록
  @PostMapping("/wishlist/connect")
  public String wishlistConnect(@Valid @ModelAttribute CreateDeleteWishlistAndAlarmRequest request
  ) throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/wishlist/");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity =
        new RequestEntity<>(mapper.writeValueAsString(request),
            headers, HttpMethod.POST, uri);

    restTemplate.exchange(requestEntity, String.class);

    restService.post(uri.toString(), mapper.convertValue(request, Map.class), String.class);

    return "redirect:/product/view/" + request.getProductId();
  }

  //찜 위시리스트 삭제
  @PostMapping("/wishlist/disconnect")
  public String wishlistDisconnect(
      @Valid @ModelAttribute CreateDeleteWishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/wishlist/");

    restService.delete(uri.toString(), mapper.convertValue(request, Map.class));

    return "redirect:/product/view/" + request.getProductId();
  }

  //재입고 알람 등록
  @PostMapping("/alarm/connect")
  public String alarmConnect(@Valid @ModelAttribute CreateDeleteWishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/alarm/");

    restService.post(uri.toString(), mapper.convertValue(request, Map.class), String.class);

    return "redirect:/product/view/" + request.getProductId();
  }

  //재입고 알람 등록 해제
  @PostMapping("/alarm/disconnect")
  public String alarmDisconnect(
      @Valid @ModelAttribute CreateDeleteWishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/alarm/");

    restService.delete(uri.toString(), mapper.convertValue(request, Map.class));

    return "redirect:/product/view/" + request.getProductId();
  }

}
