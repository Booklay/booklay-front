package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductBookJson;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateProductBookRequest;
import java.io.IOException;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class ProductController {

  private final RestTemplate restTemplate;
  private final String gatewayIp;

  @GetMapping
  public String getProductMainPage() {
    return "/admin/product/productMainManage";
  }

  @GetMapping("/book/create")
  public String getProductBookForm() {
    return "/admin/product/createProductBookForm";
  }

//  @PostMapping("/book/create")
//  public String createProductBook(@Valid @ModelAttribute CreateProductBookRequest request)
//      throws IOException {
//    log.info("제목 : " + request.getTitle());
//    log.info("ISBN : " + request.getIsbn());
//    log.info("긴설명 : " + request.getLongDescription());
//    log.info("짧설명 : " + request.getShortDescription());
//    log.info("페이지 : " + request.getPage());
//    log.info("작가리스트 : " + request.getAuthorIds().get(0));
//    log.info(
//        "사진 :" + request.getImage().getOriginalFilename() + request.getImage().getContentType());
//
//    URI uri = URI.create(gatewayIp + "/shop/v1/admin/product/register/book");
//
//    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
//
//    CreateProductBookJson jsonString = new CreateProductBookJson(request);
//
//    MultipartFile image = request.getImage();
//
//    LinkedMultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
//    headerMap.add("Content-disposition",
//        "form-data; name=image; filename=" + image.getOriginalFilename());
//    headerMap.add("Content-type", "application/octet-stream");
//    HttpEntity<byte[]> imageBytes = new HttpEntity<>(image.getBytes(), headerMap);
//
//    LinkedMultiValueMap<String, Object> multipartReqMap = new LinkedMultiValueMap<>();
//    multipartReqMap.add("jsonString", mapper.writeValueAsString(jsonString));
////    multipartReqMap.add("jsonString", jsonString);
//    multipartReqMap.add("image", imageBytes);
//
//    HttpHeaders headers = new HttpHeaders();
//    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//
//    RequestEntity<LinkedMultiValueMap<String, Object>> requestEntity = new RequestEntity<>(
//        multipartReqMap, headers, HttpMethod.POST, uri);
//
//    log.info("출력 : " + requestEntity.getBody().get("jsonString"));
//    log.info("출력 : " + requestEntity.getBody().get("image"));
//
//    ResponseEntity<Long> responseEntity =restTemplate.exchange(requestEntity, Long.class);
//
////    HttpEntity<?> uploadEntity = new HttpEntity<>(multipartReqMap, headers);
////    restTemplate.exchange(uri, HttpMethod.POST, uploadEntity, String.class);
//
//    Long productId = responseEntity.getBody();
//    log.info("생성된 상품 번호 : " + productId);
//    return "redirect:/admin/product";
//  }

  @PostMapping("/book/create")
  public String createProductBook(@Valid @ModelAttribute CreateProductBookRequest request)
      throws IOException {
    log.info("제목 : " + request.getTitle());
    log.info("ISBN : " + request.getIsbn());
    log.info("긴설명 : " + request.getLongDescription());
    log.info("짧설명 : " + request.getShortDescription());
    log.info("페이지 : " + request.getPage());
    log.info("작가리스트 : " + request.getAuthorIds().get(0));
    log.info(
        "사진 :" + request.getImage().getOriginalFilename() + request.getImage().getContentType());

    URI uri = URI.create(gatewayIp + "/shop/v1/admin/product/register/book");

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    CreateProductBookJson jsonString = new CreateProductBookJson(request);

    MultipartFile image = request.getImage();

    ByteArrayResource contentsAsResource = new ByteArrayResource(image.getBytes()){
      @Override
      public String getFilename(){
        return image.getOriginalFilename();
      }
    };

    LinkedMultiValueMap<String, Object> multipartReqMap = new LinkedMultiValueMap<>();
    multipartReqMap.add("jsonString", mapper.writeValueAsString(jsonString));
//    multipartReqMap.add("jsonString", jsonString);
    multipartReqMap.add("image", contentsAsResource);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    RequestEntity<LinkedMultiValueMap<String, Object>> requestEntity = new RequestEntity<>(
        multipartReqMap, headers, HttpMethod.POST, uri);

    log.info("출력 : " + requestEntity.getBody().get("jsonString"));
    log.info("출력 : " + requestEntity.getBody().get("image"));

    ResponseEntity<Long> responseEntity =restTemplate.exchange(requestEntity, Long.class);

//    HttpEntity<?> uploadEntity = new HttpEntity<>(multipartReqMap, headers);
//    restTemplate.exchange(uri, HttpMethod.POST, uploadEntity, String.class);

    Long productId = responseEntity.getBody();
    log.info("생성된 상품 번호 : " + productId);
    return "redirect:/admin/product";
  }

  @GetMapping("/subscribe/create")
  public String getProductSubscribeForm() {
    return "/admin/product/createProductSubscribeForm";
  }


  @GetMapping("/author")
  public String getAuthorMaintain() {
    return "/admin/product/adminAuthor";
  }
}
