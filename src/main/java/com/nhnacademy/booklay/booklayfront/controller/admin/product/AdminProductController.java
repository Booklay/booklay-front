package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategorySteps;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateDeleteProductRecommendRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductSubscribeRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.UpdateProductBookRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.UpdateProductSubscribeRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.category.CategoryService;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 최규태
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class AdminProductController extends BaseController {

  private static final Long DEFAULT_POINT_RATE = 5L;
  private static final Integer SIZE = 20;
  private static final String SHOP_PRE_FIX = "/shop/v1/";
  private static final String PRE_FIX = "admin/product";
  private static final String REDIRECT_PRE_FIX = "redirect:/admin/product/view";
  private static final String SUBSCRIBE_CONNECT_PRE_FIX = "/subscribes/connect/";
  private final RestTemplate restTemplate;
  private final String gatewayIp;
  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
  private final RestService restService;

  private final CategoryService categoryService;

  /**
   * 상품 총괄 관리 리스트 페이지
   *
   * @param model
   * @param cid
   * @param page
   * @return
   */
  @GetMapping
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
        gatewayIp + SHOP_PRE_FIX + PRE_FIX);

    ApiEntity<PageResponse<RetrieveProductResponse>> productResponse = restService.get(
        uri.toString(), getDefaultPageMap(page,SIZE), new ParameterizedTypeReference<>() {
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

    return "admin/product/display";
  }

  /**
   * 관리용 상품 상세 보기
   *
   * @param productNo
   * @param model
   * @return
   */
  @GetMapping("/view/{productNo}")
  public String productViewer(@PathVariable("productNo") Long productNo, Model model) {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    //최초 상품 상세 정보 호출
    URI mainUri = URI.create(gatewayIp + SHOP_PRE_FIX + "/product/view/" + productNo);

    ApiEntity<ProductAllInOneResponse> response = restService.get(mainUri.toString(), null,
        ProductAllInOneResponse.class);

    model.addAttribute("productNo", productNo);
    model.addAttribute("product", response.getBody());

    //연관 상품 목록 호출
    URI recommendUri = URI.create(gatewayIp + SHOP_PRE_FIX + "/product/recommend/" + productNo);

    ApiEntity<List<RetrieveProductResponse>> recommendGoodsResponse = restService.get(
        recommendUri.toString(), null, new ParameterizedTypeReference<>() {
        });

    model.addAttribute("recommendProducts", recommendGoodsResponse.getBody());

    //구독 상품의 경우 구독의 자식 상품들 목록 호출
    if (Objects.nonNull(response.getBody().getSubscribe())) {
      Long subscribeId = response.getBody().getSubscribe().getId();

      URI uriForSubscribe = URI.create(
          gatewayIp + SHOP_PRE_FIX + "/product/view/subscribe/" + subscribeId);

      ApiEntity<List<RetrieveProductResponse>> subscribeResponse = restService.get(
          uriForSubscribe.toString(), null, new ParameterizedTypeReference<>() {
          });

      model.addAttribute("booksAtSubscribe", subscribeResponse.getBody());
    }

    return "admin/product/view";
  }


  /**
   * 상품 soft delete
   *
   * @param productId
   * @return
   */
  @PostMapping("/delete/{productId}")
  public String getProductSoftDelete(@PathVariable Long productId) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + productId);

    restService.delete(uri.toString());

    return "redirect:/product/display";

  }

  /**
   * 카테고리 팝업 호출
   *
   * @param page
   * @param model
   * @return
   */
  @GetMapping("/category/popup")
  public String retrieveCategoryPopUp(
      @RequestParam(value = "page", defaultValue = "0") int page, Model model) {

    String query = "?page=" + page;
    String redirectGatewayPrefix = gatewayIp + SHOP_PRE_FIX + "/admin/categories";
    URI uri = URI.create(redirectGatewayPrefix + query);

    ApiEntity<PageResponse<CategoryResponse>> response = restService.get(uri.toString(), null,
        new ParameterizedTypeReference<>() {
        });

    if (response.isSuccess()) {
      model.addAttribute("list", response.getBody().getData());
      setCurrentPageAndMaxPageToModel(model, response.getBody());
    } else {
      return "index";
    }

    return "admin/product/category/popup";

  }

  /**
   * 카테고리 수정 팝업 호출
   *
   * @param page
   * @param model
   * @return
   */
  @GetMapping("/category/popup/{inputId}")
  public String retrieveCategoryReselectPopUp(
      @RequestParam(value = "page", defaultValue = "0") int page, Model model,
      @PathVariable String inputId) {

    String query = "?page=" + page;

    String redirectGatewayPrefix = gatewayIp + SHOP_PRE_FIX + "admin/categories";

    URI uri = URI.create(redirectGatewayPrefix + query);

    ApiEntity<PageResponse<CategoryResponse>> response = restService.get(uri.toString(), null,
        new ParameterizedTypeReference<>() {
        });

    if (response.isSuccess()) {
      setCurrentPageAndMaxPageToModel(model, response.getBody());
      model.addAttribute("list", response.getBody().getData());
      model.addAttribute("inputId", inputId);
    } else {
      return "index";
    }

    return "admin/product/popup/categoryReselectPopup";

  }

  /**
   * 책 생성 페이지 조회
   *
   * @param model
   * @return
   */
  @GetMapping("/create/books")
  public String getProductBookCreateForm(Model model) {
    model.addAttribute("defaultPointRate", DEFAULT_POINT_RATE);
    model.addAttribute(TARGET_VIEW, "product/createProductBookForm");
    return "admin/product/create/books";
  }

  /**
   * 책 생성 요청
   *
   * @param request
   * @param image
   * @return
   * @throws IOException
   */
  @PostMapping("/create/books")
  public String createProductBook(@Valid @ModelAttribute CreateProductBookRequest request,
      MultipartFile image)
      throws IOException {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/books");

    ByteArrayResource contentsAsResource = new ByteArrayResource(image.getBytes()) {
      @Override
      public String getFilename() {
        return image.getOriginalFilename();
      }
    };

    MultipartBodyBuilder resource = new MultipartBodyBuilder();
    resource.part("request", mapper.writeValueAsString(request), MediaType.APPLICATION_JSON);
    resource.part("imgFile", contentsAsResource, MediaType.valueOf(image.getContentType()));

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();
    HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody,
        headers);

    ResponseEntity<Long> responseEntity = restTemplate.postForEntity(uri, httpEntity,
        Long.class);

    Long productNo = responseEntity.getBody();
    return REDIRECT_PRE_FIX + "/" + productNo;
  }

  /**
   * 책 수정 페이지 조회
   *
   * @param model
   * @param productId
   * @return
   */
  //책 수정 페이지 조회
  @GetMapping("/update/books/{productId}")
  public String getProductBookUpdateForm(Model model, @PathVariable Long productId) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/books/" + productId);

    ApiEntity<ProductAllInOneResponse> productData = restService.get(uri.toString(),
        null, new ParameterizedTypeReference<>() {
        });

    model.addAttribute("product", productData.getBody());

    return PRE_FIX + "/update/books";
  }

  /**
   * 책 수정 요청
   *
   * @param request
   * @param image
   * @return
   * @throws IOException
   */
  @PostMapping("/update/books")
  public String updateProductBook(@Valid @ModelAttribute UpdateProductBookRequest request,
      MultipartFile image)
      throws IOException {
    log.info("진입 확인");
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/books");

    ByteArrayResource contentsAsResource = new ByteArrayResource(image.getBytes()) {
      @Override
      public String getFilename() {
        return image.getOriginalFilename();
      }
    };

    MultipartBodyBuilder resource = new MultipartBodyBuilder();
    resource.part("request", mapper.writeValueAsString(request), MediaType.APPLICATION_JSON);
    resource.part("imgFile", contentsAsResource, MediaType.MULTIPART_FORM_DATA);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();
    HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody,
        headers);

    restTemplate.put(uri, httpEntity);

    return REDIRECT_PRE_FIX + "/" + request.getProductId();
  }

  /**
   * 구독 상품 생성 페이지 조회
   *
   * @param model
   * @return
   */
  @GetMapping("/create/subscribes")
  public String getProductSubscribeCreateForm(Model model) {
    model.addAttribute("defaultPointRate", DEFAULT_POINT_RATE);
    model.addAttribute(TARGET_VIEW, "product/createProductSubscribeForm");
    return "admin/product/create/subscribes";
  }

  /**
   * 구독 상품 생성 요청
   *
   * @param request
   * @param image
   * @return
   * @throws IOException
   */
  @PostMapping("/create/subscribes")
  public String createProductSubscribe(@Valid @ModelAttribute CreateProductSubscribeRequest request,
      MultipartFile image) throws IOException {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/subscribes");
    ByteArrayResource contentsAsResource = new ByteArrayResource(image.getBytes()) {
      @Override
      public String getFilename() {
        return image.getOriginalFilename();
      }
    };

    MultipartBodyBuilder resource = new MultipartBodyBuilder();
    resource.part("request", mapper.writeValueAsString(request), MediaType.APPLICATION_JSON);
    resource.part("imgFile", contentsAsResource, MediaType.MULTIPART_FORM_DATA);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();
    HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody,
        headers);

    ResponseEntity<Long> responseEntity = restTemplate.postForEntity(uri, httpEntity,
        Long.class);

    Long productNo = responseEntity.getBody();
    return REDIRECT_PRE_FIX + "/" + productNo;
  }

  /**
   * 구독 상품 수정 페이지 조회
   *
   * @param model
   * @param productId
   * @return
   */
  @GetMapping("/update/subscribes/{productId}")
  public String getProductSubscribeUpdateForm(Model model, @PathVariable Long productId) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/subscribes/" + productId);

    ApiEntity<ProductAllInOneResponse> productData = restService.get(uri.toString(),
        null, new ParameterizedTypeReference<>() {
        });

    model.addAttribute("product", productData.getBody());

    return PRE_FIX + "/update/subscribes";
  }

  /**
   * 구독 상품 수정 요청
   *
   * @param request
   * @param image
   * @return
   * @throws IOException
   */
  @PostMapping("/update/subscribes")
  public String updateProductSubscribe(@Valid @ModelAttribute UpdateProductSubscribeRequest request,
      MultipartFile image)
      throws IOException {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/subscribes");

    ByteArrayResource contentsAsResource = new ByteArrayResource(image.getBytes()) {
      @Override
      public String getFilename() {
        return image.getOriginalFilename();
      }
    };

    MultipartBodyBuilder resource = new MultipartBodyBuilder();
    resource.part("request", mapper.writeValueAsString(request), MediaType.APPLICATION_JSON);
    resource.part("imgFile", contentsAsResource, MediaType.MULTIPART_FORM_DATA);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();
    HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody,
        headers);

    restTemplate.put(uri, httpEntity);

    return REDIRECT_PRE_FIX + "/" + request.getProductId();
  }

  /**
   * 작가 팝업창 호출
   *
   * @return
   */
  @GetMapping("/author")
  public String getAuthorMaintain() {
    return PRE_FIX + "/adminAuthor";
  }

  /**
   * 구독 상품의 하위상품 등록용 조회
   *
   * @param model
   * @param page
   * @param subscribeId
   * @return
   */
  @GetMapping("/subscribes/connect/{subscribeId}")
  public String getBooksForSubscribe(Model model,
      @RequestParam(value = "page", defaultValue = "0") int page,
      @PathVariable Long subscribeId) {

    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX + PRE_FIX + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId);

    ApiEntity<PageResponse<RetrieveBookForSubscribeResponse>> bookResponse = restService.get(
        uri.toString(), getDefaultPageMap(page,SIZE), new ParameterizedTypeReference<>() {
        });

    List<RetrieveBookForSubscribeResponse> bookList = bookResponse.getBody().getData();

    setCurrentPageAndMaxPageToModel(model, bookResponse.getBody());
    model.addAttribute("subscribeId", subscribeId);
    model.addAttribute("bookList", bookList);

    return PRE_FIX + "/subscribes";
  }

  /**
   * 구독 상품 하위 상품 등록 요청
   *
   * @param request
   * @param subscribeId
   * @param pageNum
   * @return
   */
  @PostMapping("/subscribes/connect/{subscribeId}/{pageNum}")
  public String subscribeBookConnection(
      @Valid @ModelAttribute DisAndConnectBookWithSubscribeRequest request,
      @PathVariable Long subscribeId, @PathVariable Long pageNum) {
    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX + PRE_FIX + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId);

    restService.post(uri.toString(), mapper.convertValue(request, Map.class),
        CreateDeleteTagProductRequest.class);

    return "redirect:/admin/product" + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId + "?page=" + pageNum
        + "&size=" + SIZE;
  }

  /**
   * 구독 상품 하위 상품 등록 취소 요청
   *
   * @param subscribeId
   * @param request
   * @param pageNum
   * @return
   */
  @PostMapping("/subscribes/disconnect/{subscribeId}/{pageNum}")
  public String subscribeBookDisconnection(@PathVariable Long subscribeId,
      @Valid @ModelAttribute DisAndConnectBookWithSubscribeRequest request,
      @PathVariable Long pageNum) {

    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX + PRE_FIX + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId);

    restService.delete(uri.toString(), mapper.convertValue(request, Map.class));

    return "redirect:/admin/product" + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId + "?page=" + pageNum
        + "&size=" + SIZE;
  }

  /**
   * 연관상품 등록화면 호출
   *
   * @param page
   * @param productNo
   * @param model
   * @return
   */
  @GetMapping("/recommend/{productNo}")
  public String retrieveProductForRecommend(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @PathVariable Long productNo, Model model) {

    //최초 상품 상세 정보 호출
    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/recommend/" + productNo);

    ApiEntity<PageResponse<RetrieveProductResponse>> recommendResponse = restService.get(
        uri.toString(), getDefaultPageMap(page,SIZE), new ParameterizedTypeReference<>() {
        });

    setCurrentPageAndMaxPageToModel(model, recommendResponse.getBody());
    model.addAttribute("productNo", productNo);
    model.addAttribute("productList", recommendResponse.getBody().getData());

    return PRE_FIX + "/relation";
  }

  /**
   * 연관 상품 등록 요청
   *
   * @param request
   * @param pageNum
   * @return
   */
  @PostMapping("/recommend/create/{pageNum}")
  public String createRecommend(@Valid @ModelAttribute CreateDeleteProductRecommendRequest request,
      @PathVariable Long pageNum) {
    Long productNo = request.getBaseId();

    log.info("진입 확인");

    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/recommend");

    restService.post(uri.toString(), mapper.convertValue(request, Map.class),
        CreateDeleteProductRecommendRequest.class);

    return "redirect:/admin/product/recommend/" + productNo + "?page=" + pageNum + "&size=" + SIZE;
  }

  /**
   * 연관 상품 등록 취소 요청
   *
   * @param request
   * @param pageNum
   * @return
   */
  @PostMapping("/recommend/delete/{pageNum}")
  public String deleteRecommend(@Valid @ModelAttribute CreateDeleteProductRecommendRequest request,
      @PathVariable Long pageNum) {
    Long productNo = request.getBaseId();

    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + PRE_FIX + "/recommend");

    restService.delete(uri.toString(), mapper.convertValue(request, Map.class));

    return "redirect:/admin/product/recommend/" + productNo + "?page=" + pageNum + "&size=" + SIZE;
  }
}