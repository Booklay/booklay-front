package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductBookRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductSubscribeRequest;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 최규태
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class AdminProductController {

  private static final String PRE_FIX = "/admin/product";
  private static final String URI_PRE_FIX = "/shop/v1/admin/product/";
  private final RestTemplate restTemplate;
  private final String gatewayIp;
  private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
  private final Long DEFAULT_POINT_RATE = 5L;

  @GetMapping
  public String getProductMainPage() {
    return PRE_FIX + "/productMainManage";
  }

  @GetMapping("/books/create")
  public String getProductBookCreateForm(Model model) {
    model.addAttribute("defaultPointRate", DEFAULT_POINT_RATE);
    return PRE_FIX + "/createProductBookForm";
  }

  @GetMapping("/books/update/{productId}")
  public String getProductBookUpdateForm(Model model, @PathVariable Long productId) {
    URI uri = URI.create(gatewayIp + URI_PRE_FIX + "books");

    return PRE_FIX + "/updateProductBookForm";
  }

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
    resource.part("imgFile", contentsAsResource, MediaType.MULTIPART_FORM_DATA);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();
    HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody,
        headers);

    ResponseEntity<Long> responseEntity = restTemplate.postForEntity(uri, httpEntity,
        Long.class);

    Long productNo = responseEntity.getBody();
    return "redirect:/product/view/" + productNo;
  }

  @GetMapping("/subscribes/create")
  public String getProductSubscribeCreateForm(Model model) {
    model.addAttribute("defaultPointRate", DEFAULT_POINT_RATE);
    return PRE_FIX + "/createProductSubscribeForm";
  }

  @GetMapping("/subscribes/update/{productId}")
  public String getProductSubscribeUpdateForm(Model model, @PathVariable Long productId) {

    return PRE_FIX + "/updateProductSubscribeForm";
  }


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
    return "redirect:/product/view/" + productNo;
  }


  @GetMapping("/author")
  public String getAuthorMaintain() {

    return PRE_FIX + "/adminAuthor";
  }
}
