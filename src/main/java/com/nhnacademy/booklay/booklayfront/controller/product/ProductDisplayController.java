package com.nhnacademy.booklay.booklayfront.controller.product;

import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategorySteps;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request.CreateDeleteWishlistAndAlarmRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.wishlist.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.category.CategoryService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
  private final RestService restService;
  private final ObjectMapper mapper = new ObjectMapper();
  private final CategoryService categoryService;
  private final Integer SIZE = 20;

  //게시판형 전채 상품 호출
  @GetMapping("/display")
  public String retrieveProduct(Model model,
      @RequestParam(value = "CID", required = false) Long cid,
      @RequestParam(value = "page", defaultValue = "0") int page) {

    List<CategorySteps> categorySteps = (List<CategorySteps>) model.getAttribute("categories");

    if (Objects.isNull(cid)) {
      cid = categoryService.getDefaultCategoryId(categorySteps);
    }

    CategorySteps currentCategory = categoryService.getCurrentCategory(categorySteps, cid);

    log.error(" currentCategory : {}", currentCategory);

    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX );

    ApiEntity<PageResponse<ProductAllInOneResponse>> productResponse = restService.get(
        uri.toString(), getDefaultPageMap(page,SIZE), new ParameterizedTypeReference<>() {
        });

    if (Objects.nonNull(productResponse.getBody())) {
      int totalPage = productResponse.getBody().getTotalPages();
      int currentPage = productResponse.getBody().getPageNumber();
      List<ProductAllInOneResponse> productList = productResponse.getBody().getData();

      model.addAttribute("currentPage", currentPage);
      model.addAttribute("totalPage", totalPage);
      model.addAttribute("productList", productList);
      model.addAttribute("currentCategory", currentCategory);
    }

    return "product/display";
  }

  //상품 상세 보기
  @GetMapping("/view/{productNo}")
  public String productViewer(@PathVariable("productNo") Long productNo, Model model,
      MemberInfo memberInfo) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    //최초 상품 상세 정보 호출
    URI mainUri = URI.create(gatewayIp + SHOP_PRE_FIX + "/view/" + productNo);

    ApiEntity<ProductAllInOneResponse> response = restService.get(mainUri.toString(), null,
        ProductAllInOneResponse.class);

    model.addAttribute("productNo", productNo);
    model.addAttribute("product", response.getBody());

    //연관 상품 목록 호출
    URI recommendUri = URI.create(gatewayIp + SHOP_PRE_FIX + "/recommend/" + productNo);

    ApiEntity<List<RetrieveProductResponse>> recommendGoodsResponse = restService.get(
        recommendUri.toString(), null, new ParameterizedTypeReference<>() {
        });

    model.addAttribute("recommendProducts", recommendGoodsResponse.getBody());

    //구독 상품의 경우 구독의 자식 상품들 목록 호출
    if (Objects.nonNull(response.getBody().getSubscribe())) {
      Long subscribeId = response.getBody().getSubscribe().getId();

      URI uriForSubscribe = URI.create(gatewayIp + SHOP_PRE_FIX + "/view/subscribe/" + subscribeId);

      ApiEntity<List<RetrieveProductResponse>> subscribeResponse = restService.get(
          uriForSubscribe.toString(), null, new ParameterizedTypeReference<>() {
          });

      model.addAttribute("booksAtSubscribe", subscribeResponse.getBody());
    }
    //위시리스트, 알림 등록 확인
    model.addAttribute("thisMember", memberInfo);
    if (memberInfo.getMemberNo() != null) {
      URI uriForMember = URI.create(
          gatewayIp + "/shop/v1/mypage/product/boolean/" + memberInfo.getMemberNo());

      ApiEntity<WishlistAndAlarmBooleanResponse> wishlistAndAlarmResponse = restService.get(
          uriForMember.toString(), null, WishlistAndAlarmBooleanResponse.class);

      model.addAttribute("memberProduct", wishlistAndAlarmResponse.getBody());
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
  public String wishlistConnect(
      @Valid @ModelAttribute CreateDeleteWishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/wishlist/");

    restService.post(uri.toString(), mapper.convertValue(request, Map.class), Void.class);

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

    restService.post(uri.toString(), mapper.convertValue(request, Map.class),
        CreateDeleteWishlistAndAlarmRequest.class);

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
