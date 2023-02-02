package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateDeleteProductRecommendRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductSubscribeRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.DisAndConnectBookWithSubscribeRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.UpdateProductBookRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.UpdateProductSubscribeRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveBookForSubscribeResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductBookForUpdateResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductSubscribeForUpdateResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * @author 최규태
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class AdminProductController {

  private static final Long DEFAULT_POINT_RATE = 5L;
  private static final String PRE_FIX = "admin/product";
  private static final String URI_PRE_FIX = "/shop/v1/admin/product/";
  private static final String REDIRECT_PRE_FIX = "redirect:/product/view/";
  private static final String RETURN_PAGE = "admin/adminPage";
  private static final String SUBSCRIBE_CONNECT_PRE_FIX = "/subscribes/connect/";
  private final RestTemplate restTemplate;
  private final String gatewayIp;
  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
  private final RestService restService;

  //총 관리 인덱스 페이지
  @GetMapping
  public String getProductMainPage() {
    return PRE_FIX + "/productMainManage";
  }

  //상품 soft delete
  @PostMapping("/delete/{productId}")
  public String getProductSoftDelete(@PathVariable Long productId) {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + productId);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

    RequestEntity<Long> requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.DELETE, uri);

    restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
    });

    return "redirect:/product/display";
  }

  //카테고리 팝업 호출
  @GetMapping("/category/popup")
  public String retrieveCategoryPopUp(
      @RequestParam(value = "page", defaultValue = "0") int page, Model model) {

    String query = "?page=" + page;

    String redirectGatewayPrefix = gatewayIp + "/shop/v1" + "/admin/categories";

    URI uri = URI.create(redirectGatewayPrefix + query);

    ApiEntity<PageResponse<CategoryResponse>> response = restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
        });

    if (response.isSuccess()) {
      model.addAttribute("list", response.getBody().getData());
      model.addAttribute("totalPage", response.getBody().getTotalPages());
      model.addAttribute("currentPage", response.getBody().getPageNumber());
    } else {
      return "/index";
    }

    return "admin/product/popup/categoryPopup";
  }

  //책 생성 페이지
  @GetMapping("/books/create")
  public String getProductBookCreateForm(Model model) {
    model.addAttribute("defaultPointRate", DEFAULT_POINT_RATE);
    model.addAttribute(TARGET_VIEW, "product/createProductBookForm");
    return RETURN_PAGE;
  }

  //책 생성 요청
  @PostMapping("/books/create")
  public String createProductBook(@Valid @ModelAttribute CreateProductBookRequest request,
      MultipartFile image)
      throws IOException {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "books");

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
    return REDIRECT_PRE_FIX + productNo;
  }

  //책 수정 페이지 조회
  @GetMapping("/books/update/{productId}")
  public String getProductBookUpdateForm(Model model, @PathVariable Long productId) {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "books/" + productId);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

    RequestEntity<Long> requestEntity = new RequestEntity<>(
        httpHeaders, HttpMethod.GET, uri);

    ResponseEntity<RetrieveProductBookForUpdateResponse> response =
        restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

    RetrieveProductBookForUpdateResponse productData = response.getBody();

    model.addAttribute("product", productData);

    return PRE_FIX + "/updateProductBookForm";
  }

  //책 수정 요청
  @PostMapping("/books/update")
  public String updateProductBook(@Valid @ModelAttribute UpdateProductBookRequest request,
      MultipartFile image)
      throws IOException {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "books");

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

    return REDIRECT_PRE_FIX + request.getProductId();
  }

  //구독 상품 생성 페이지 조회
  @GetMapping("/subscribes/create")
  public String getProductSubscribeCreateForm(Model model) {
    model.addAttribute("defaultPointRate", DEFAULT_POINT_RATE);
    model.addAttribute(TARGET_VIEW, "product/createProductSubscribeForm");
    return RETURN_PAGE;
  }

  //구독 상품 생성
  @PostMapping("/subscribes/create")
  public String createProductSubscribe(@Valid @ModelAttribute CreateProductSubscribeRequest request,
      MultipartFile image) throws IOException {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "subscribes");
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
    return REDIRECT_PRE_FIX + productNo;
  }

  //구독 상품 수정 페이지 조회
  @GetMapping("/subscribes/update/{productId}")
  public String getProductSubscribeUpdateForm(Model model, @PathVariable Long productId) {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "subscribes/" + productId);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

    RequestEntity<Long> requestEntity = new RequestEntity<>(
        httpHeaders, HttpMethod.GET, uri);

    ResponseEntity<RetrieveProductSubscribeForUpdateResponse> response =
        restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

    RetrieveProductSubscribeForUpdateResponse productData = response.getBody();

    model.addAttribute("product", productData);

    return PRE_FIX + "/updateProductSubscribeForm";
  }

  //구독 수정 요청
  @PostMapping("/subscribes/update")
  public String updateProductSubscribe(@Valid @ModelAttribute UpdateProductSubscribeRequest request,
      MultipartFile image)
      throws IOException {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "subscribes");

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

    return REDIRECT_PRE_FIX + request.getProductId();
  }

  //작가 팝업창
  @GetMapping("/author")
  public String getAuthorMaintain() {
    return PRE_FIX + "/adminAuthor";
  }

  //구독 상품 하위 상품 등록용 조회
  @GetMapping("/subscribes/connect/{subscribeId}/{pageNum}")
  public String getBooksForSubscribe(Model model, @PathVariable Long pageNum,
      @PathVariable Long subscribeId) {
    if (pageNum < 0L) {
      pageNum = 1L;
    }

    Long size = 20L;

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

    URI uri = URI.create(
        gatewayIp + URI_PRE_FIX + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId + "?page=" + pageNum
            + "&size=" + size);
    RequestEntity<PageResponse<RetrieveTagResponse>> requestEntity = new RequestEntity<>(
        httpHeaders, HttpMethod.GET, uri);

    ResponseEntity<PageResponse<RetrieveBookForSubscribeResponse>> bookResponse =
        restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

    int totalPage = bookResponse.getBody().getTotalPages();
    int nowPage = bookResponse.getBody().getPageNumber();
    List<RetrieveBookForSubscribeResponse> bookList = bookResponse.getBody().getData();

    model.addAttribute("subscribeId", subscribeId);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("totalPage", totalPage);
    model.addAttribute("bookList", bookList);

    return PRE_FIX + "/productSubscribeConnector";
  }

  //구독 상품 하위 상품 등록
  @PostMapping("/subscribes/connect/{subscribeId}/{pageNum}")
  public String subscribeBookConnection(@PathVariable Long pageNum,
      @PathVariable Long subscribeId,
      @Valid @ModelAttribute DisAndConnectBookWithSubscribeRequest request)
      throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.POST, uri);

    restTemplate.exchange(requestEntity, CreateDeleteTagProductRequest.class);
    return "redirect:/admin/product" + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId + "/" + pageNum;
  }

  //구독 상품 하위 상품 등록 취소
  @PostMapping("/subscribes/disconnect/{subscribeId}/{pageNum}")
  public String subscribeBookDisconnection(@PathVariable Long pageNum,
      @PathVariable Long subscribeId,
      @Valid @ModelAttribute DisAndConnectBookWithSubscribeRequest request)
      throws JsonProcessingException {

    URI uri = URI.create(gatewayIp + URI_PRE_FIX + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.DELETE, uri);

    restTemplate.exchange(requestEntity, CreateDeleteTagProductRequest.class);

    return "redirect:/admin/product" + SUBSCRIBE_CONNECT_PRE_FIX + subscribeId + "/" + pageNum;
  }

  //연관 상품 등록 위해 조회
  @GetMapping("/recommend/{productNo}/{pageNum}")
  public String retrieveProductForRecommend(@PathVariable Long pageNum,
      @PathVariable Long productNo, Model model) {
    Long size = 20L;

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    //최초 상품 상세 정보 호출
    URI uri = URI.create(
        gatewayIp + URI_PRE_FIX + "/recommend/" + productNo + "?page=" + pageNum + "&size=" + size);

    RequestEntity<String> requestEntity = new RequestEntity<>(null,
        headers, HttpMethod.GET, uri);

    ResponseEntity<PageResponse<RetrieveProductResponse>> recommendResponse =
        restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

    int totalPage = recommendResponse.getBody().getTotalPages();
    int nowPage = recommendResponse.getBody().getPageNumber();
    List<RetrieveProductResponse> productList = recommendResponse.getBody().getData();

    model.addAttribute("productNo", productNo);
    model.addAttribute("productList", productList);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("totalPage", totalPage);

    return PRE_FIX + "/recommendConnector";
  }

  //연관 상품 등록
  @PostMapping("/recommend/create/{pageNum}")
  public String createRecommend(@Valid @ModelAttribute CreateDeleteProductRecommendRequest request,
      @PathVariable Long pageNum) throws JsonProcessingException {
    Long productNo = request.getBaseId();

    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "/recommend");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.POST, uri);

    restTemplate.exchange(requestEntity, CreateDeleteProductRecommendRequest.class);

    return "redirect:/admin/product/recommend/" + productNo + "/" + pageNum;
  }

  //연관 상품 삭제
  @PostMapping("/recommend/delete/{pageNum}")
  public String deleteRecommend(@Valid @ModelAttribute CreateDeleteProductRecommendRequest request,
      @PathVariable Long pageNum) throws JsonProcessingException {
    Long productNo = request.getBaseId();

    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "/recommend");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.DELETE, uri);

    restTemplate.exchange(requestEntity, CreateDeleteProductRecommendRequest.class);

    return "redirect:/admin/product/recommend/" + productNo + "/" + pageNum;
  }
}
