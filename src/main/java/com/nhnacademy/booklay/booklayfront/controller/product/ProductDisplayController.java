package com.nhnacademy.booklay.booklayfront.controller.product;

import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.board.response.PostResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategorySteps;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request.WishlistAndAlarmRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.wishlist.response.WishlistAndAlarmBooleanResponse;
import com.nhnacademy.booklay.booklayfront.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.ReviewService;
import com.nhnacademy.booklay.booklayfront.service.category.CategoryService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author ?????????
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductDisplayController extends BaseController {

  private static final String SHOP_PRE_FIX = "/shop/v1/product";
  private final String gatewayIp;
  private final RestService restService;
  private final ObjectMapper objectMapper;
  private final CategoryService categoryService;
  private final ReviewService reviewService;
  private static final Integer SIZE = 20;
  private static final String REDIRECT_HTML_PREFIX = "redirect:/product/view/";

  /**
   * ????????? ?????? ?????? ??????
   * @param model
   * @param cid
   * @param page
   * @return
   */
  @GetMapping("/display")
  public String retrieveProduct(Model model,
      @RequestParam(value = "CID", required = false) Long cid,
      @RequestParam(value = "page", defaultValue = "0") int page) {

    List<CategorySteps> categorySteps = (List<CategorySteps>) model.getAttribute("categories");

    if (Objects.isNull(cid)) {
      cid = categoryService.getDefaultCategoryId(categorySteps);
    }

    CategorySteps currentCategory = categoryService.getCurrentCategory(categorySteps, cid);

    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX);

    ApiEntity<PageResponse<ProductAllInOneResponse>> productResponse = restService.get(
        uri.toString(), getDefaultPageMap(page, SIZE), new ParameterizedTypeReference<>() {
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

  /**
   * ?????? ?????? ??????
   * @param productNo
   * @param model
   * @param page
   * @param reviewPage
   * @param memberInfo
   * @param request
   * @return
   */
  @GetMapping("/view/{productNo}")
  public String productViewer(@PathVariable("productNo") Long productNo, Model model,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "reviewPage", defaultValue = "0") int reviewPage,
                              MemberInfo memberInfo,
                              HttpServletRequest request) {

    //?????? ?????? ?????? ?????? ??????
    URI mainUri = URI.create(gatewayIp + SHOP_PRE_FIX + "/view/" + productNo);

    ApiEntity<ProductAllInOneResponse> response = restService.get(mainUri.toString(), null,
        ProductAllInOneResponse.class);

    model.addAttribute("productNo", productNo);
    model.addAttribute("product", response.getBody());

    //?????? ?????? ?????? ??????
    URI recommendUri = URI.create(gatewayIp + SHOP_PRE_FIX + "/recommend/" + productNo);

    ApiEntity<List<RetrieveProductResponse>> recommendGoodsResponse = restService.get(
        recommendUri.toString(), null, new ParameterizedTypeReference<>() {
        });

    model.addAttribute("recommendProducts", recommendGoodsResponse.getBody());

    //?????? ????????? ?????? ????????? ?????? ????????? ?????? ??????
    if (Objects.nonNull(response.getBody().getSubscribe())) {
      Long subscribeId = response.getBody().getSubscribe().getId();

      URI uriForSubscribe = URI.create(gatewayIp + SHOP_PRE_FIX + "/view/subscribe/" + subscribeId);

      ApiEntity<List<RetrieveProductResponse>> subscribeResponse = restService.get(
          uriForSubscribe.toString(), null, new ParameterizedTypeReference<>() {
          });

      model.addAttribute("booksAtSubscribe", subscribeResponse.getBody());
    }
    //???????????????, ?????? ?????? ??????
    model.addAttribute("memberInfo", memberInfo);
    if (memberInfo.getMemberNo() != null) {
      URI uriForMember = URI.create(
          gatewayIp + "/shop/v1/mypage/product/boolean");

      WishlistAndAlarmRequest booleanRequest = new WishlistAndAlarmRequest(memberInfo.getMemberNo(),
          productNo);

      ApiEntity<WishlistAndAlarmBooleanResponse> wishlistAndAlarmResponse = restService.post(
          uriForMember.toString(), objectMapper.convertValue(booleanRequest, Map.class),
          WishlistAndAlarmBooleanResponse.class);

      log.info("?????? ?????? " + wishlistAndAlarmResponse.getBody().getAlarm() + "??????????????? ??????"
          + wishlistAndAlarmResponse.getBody().getWishlist());
      model.addAttribute("memberProduct", wishlistAndAlarmResponse.getBody());
    }

//?????? ?????? ????????? ??????
    URI qnaUri = URI.create(gatewayIp + "/shop/v1/board/product/" + productNo);
    ApiEntity<PageResponse<PostResponse>> postResponse = restService.get(qnaUri.toString(),
        getDefaultPageMap(page, 10), new ParameterizedTypeReference<>() {
        });

    model.addAttribute("postList", postResponse.getBody().getData());
    setCurrentPageAndMaxPageToModel(model, postResponse.getBody());

//   ?????? ??????
    SearchPageResponse<RetrieveReviewResponse>
        reviews = reviewService.retrieveReview(productNo, reviewPage);

    if (Objects.nonNull(reviews)) {
      model.addAttribute("reviewPage", reviewPage);
      model.addAttribute("reviewTotalPage", reviews.getTotalPages());
      model.addAttribute("reviewTotalHits", reviews.getTotalHits());
      model.addAttribute("reviews", reviews.getData());

      model.addAttribute("scoreAverage", reviews.getAverageScore());

    } else {
      model.addAttribute("reviewPage", reviewPage);
      model.addAttribute("reviews", List.of());
    }

    return "product/view";
  }


  /**
   * ???(???????????????) ??????
   * @param request
   * @return
   */
  @PostMapping("/wishlist/connect")
  public String wishlistConnect(
      @Valid @ModelAttribute WishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/wishlist/");

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class), Void.class);

    return REDIRECT_HTML_PREFIX + request.getProductId();
  }

  /**
   * ???(???????????????) ??????
   * @param request
   * @return
   */
  @PostMapping("/wishlist/disconnect")
  public String wishlistDisconnect(
      @Valid @ModelAttribute WishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/wishlist/");

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));

    return REDIRECT_HTML_PREFIX + request.getProductId();
  }

  /**
   * ????????? ?????? ??????
   * @param request
   * @return
   */
  @PostMapping("/alarm/connect")
  public String alarmConnect(@Valid @ModelAttribute WishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/alarm/");

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
        WishlistAndAlarmRequest.class);

    return REDIRECT_HTML_PREFIX + request.getProductId();
  }


  /**
   * ????????? ?????? ?????? ??????
   * @param request
   * @return
   */
  @PostMapping("/alarm/disconnect")
  public String alarmDisconnect(
      @Valid @ModelAttribute WishlistAndAlarmRequest request) {
    URI uri = URI.create(gatewayIp + "/shop/v1/mypage/product/alarm/");

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));

    return REDIRECT_HTML_PREFIX + request.getProductId();
  }

}
