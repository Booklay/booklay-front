package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.TagProductResponse;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/tag")
public class AdminTagController {

  private final String prePix = "/admin/product/tag";
  private final RestTemplate restTemplate;
  private final String gatewayIp;
  private final ObjectMapper mapper = new ObjectMapper();

  @GetMapping("/maintenance")
  public String retrieveTag(
      @RequestParam(value = "page", required = false) Optional<Integer> pageNum, Model model) {
    if (pageNum.isEmpty()) {
      pageNum = Optional.of(0);
    }
    if (pageNum.get() < 0) {
      pageNum = Optional.of(0);
    }

    Long size = 20L;

    pageNum = Optional.of(pageNum.get() - 1);

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

    URI uri = URI.create(
        gatewayIp + "/shop/v1/admin/product/tag?page=" + pageNum.get() + "&size=" + size);

    RequestEntity<PageResponse<RetrieveTagResponse>> requestEntity = new RequestEntity<>(
        httpHeaders, HttpMethod.GET, uri);

    ResponseEntity<PageResponse<RetrieveTagResponse>> testTags =
        restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

    int totalPage = testTags.getBody().getTotalPages();
    int nowPage = testTags.getBody().getPageNumber();
    List<RetrieveTagResponse> tagList = testTags.getBody().getData();

    model.addAttribute("nowPage", nowPage);
    model.addAttribute("totalPage", totalPage);
    model.addAttribute("tagList", tagList);

    return "/admin/product/adminTag";
  }

  @PostMapping("/maintenance")
  public String createTagAtMaintenance(@Valid @ModelAttribute CreateTagRequest request)
      throws JsonProcessingException {
    createTag(request);

    return "redirect:" + prePix + "/maintenance";
  }

  @PostMapping("/maintenance/update")
  public String updateTagAtMaintenance(@Valid @ModelAttribute UpdateTagRequest request)
      throws JsonProcessingException {
    updateTag(request);

    return "redirect:" + prePix + "/maintenance";
  }

  @GetMapping("/connection/{productNo}/{pageNum}")
  public String retrieveTagForProductConnect(
      @PathVariable("pageNum") Long pageNum, Model model,
      @PathVariable("productNo") Long productNo) {
    if (pageNum < 0L) {
      pageNum = 1L;
    }

    Long size = 10L;

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

    URI uri = URI.create(
        gatewayIp + "/shop/v1/admin/product/tag/product/" + productNo + "?page=" + pageNum
            + "&size=" + size);

    RequestEntity<PageResponse<TagProductResponse>> requestEntity = new RequestEntity<>(
        httpHeaders, HttpMethod.GET, uri);

    ResponseEntity<PageResponse<TagProductResponse>> testTags =
        restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

    int totalPage = testTags.getBody().getTotalPages();
    int nowPage = testTags.getBody().getPageNumber();
    List<TagProductResponse> tagList = testTags.getBody().getData();

    model.addAttribute("nowPage", nowPage);
    model.addAttribute("totalPage", totalPage);
    model.addAttribute("tagList", tagList);
    model.addAttribute("productNo", productNo);

    return "/admin/product/productTagConnector";
  }

  @PostMapping("/connection/{productNo}/{pageNum}")
  public String tagProductConnect(@PathVariable("pageNum") Long pageNum,
      @PathVariable("productNo") Long productNo,
      @Valid @ModelAttribute CreateDeleteTagProductRequest request) throws JsonProcessingException {
    if (pageNum < 0L) {
      pageNum = 1L;
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    URI uri = URI.create(gatewayIp + "/shop/v1/admin/product/tag/product");

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.POST, uri);

    log.info(requestEntity.getBody());

    restTemplate.exchange(requestEntity, CreateDeleteTagProductRequest.class);

    return "redirect:" + prePix + "/connection/" + productNo + "/" + pageNum;
  }


  @PostMapping("/connection/delete/{productNo}/{pageNum}")
  public String tagProductDisconnect(@PathVariable("pageNum") Long pageNum,
      @PathVariable("productNo") Long productNo,
      @Valid @ModelAttribute CreateDeleteTagProductRequest request) throws JsonProcessingException {
    if (pageNum < 0L) {
      pageNum = 1L;
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    URI uri = URI.create(gatewayIp + "/shop/v1/admin/product/tag/product");

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.DELETE, uri);

    log.info(requestEntity.getBody());

    restTemplate.exchange(requestEntity, CreateDeleteTagProductRequest.class);

    return "redirect:" + prePix + "/connection/" + productNo + "/" + pageNum;
  }

  @PostMapping("/connection/create/{productNo}/{pageNum}")
  public String createTagAtConnector(@Valid @ModelAttribute CreateTagRequest request,
      @PathVariable Long pageNum, @PathVariable Long productNo)
      throws JsonProcessingException {
    if (pageNum < 0L) {
      pageNum = 1L;
    }
    createTag(request);
    return "redirect:" + prePix + "/connection/" + productNo + "/" + pageNum;
  }

  @PostMapping("/connection/update/{productNo}/{pageNum}")
  public String updateTagAtConnector(@Valid @ModelAttribute UpdateTagRequest request,
      @PathVariable Long pageNum, @PathVariable Long productNo) throws JsonProcessingException {
    if (pageNum < 0L) {
      pageNum = 1L;
    }

    updateTag(request);
    return "redirect:" + prePix + "/connection/" + productNo + "/" + pageNum;
  }


  //공통 부분 리팩토링
  public void createTag(CreateTagRequest request) throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + "/shop/v1/admin/product/tag");

    log.info(request.getName());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.POST, uri);

    log.info(requestEntity.getBody());

    restTemplate.exchange(requestEntity, RetrieveTagResponse.class);
  }

  public void updateTag(UpdateTagRequest request) throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + "/shop/v1/admin/product/tag/");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.PUT, uri);

    restTemplate.exchange(requestEntity, UpdateTagRequest.class);
  }
}
