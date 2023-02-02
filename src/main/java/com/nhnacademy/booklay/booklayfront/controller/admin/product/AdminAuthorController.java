package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.DeleteByIdRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
import java.net.URI;
import java.util.List;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("/admin/author/maintenance")
public class AdminAuthorController {

  private static final String PAGE_PRE_FIX = "redirect:/admin/author/maintenance";
  private static final String SHOP_PRE_FIX = "/shop/v1/admin/author";
  private final RestTemplate restTemplate;
  private final String gatewayIp;
  private final ObjectMapper objectMapper;


  @GetMapping()
  public String authorMaintenance(
      @RequestParam(value = "page", required = false) Optional<Integer> pageNum, Model model) {

    ResponseEntity<PageResponse<RetrieveAuthorResponse>> authorPage = retrieveAuthors(pageNum);

    if (Objects.nonNull(authorPage.getBody())) {
      int totalPage = authorPage.getBody().getTotalPages();
      int nowPage = authorPage.getBody().getPageNumber();
      List<RetrieveAuthorResponse> authorList = authorPage.getBody().getData();

      model.addAttribute("nowPage", nowPage);
      model.addAttribute("totalPage", totalPage);
      model.addAttribute("authorList", authorList);
      model.addAttribute(TARGET_VIEW, "product/adminAuthor");
    }

    return "admin/adminPage";
  }


  @GetMapping("/popup")
  public String authorPopup(
      @RequestParam(value = "page", required = false) Optional<Integer> pageNum, Model model) {

    ResponseEntity<PageResponse<RetrieveAuthorResponse>> authorPage = retrieveAuthors(pageNum);

    if (Objects.nonNull(authorPage.getBody())) {
      int totalPage = authorPage.getBody().getTotalPages();
      int nowPage = authorPage.getBody().getPageNumber();
      List<RetrieveAuthorResponse> authorList = authorPage.getBody().getData();

      model.addAttribute("nowPage", nowPage);
      model.addAttribute("totalPage", totalPage);
      model.addAttribute("authorList", authorList);
      model.addAttribute(TARGET_VIEW, "product/adminAuthor");
    }

    return "admin/product/popup/authorPopup";
  }

  @PostMapping
  public String createAuthor(@Valid @ModelAttribute CreateAuthorRequest request)
      throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(
        objectMapper.writeValueAsString(request),
        headers, HttpMethod.POST, uri);

    restTemplate.exchange(requestEntity, RetrieveTagResponse.class);

    return PAGE_PRE_FIX;
  }

  @PostMapping("/update")
  public String updateAuthor(@Valid @ModelAttribute UpdateAuthorRequest request)
      throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(
        objectMapper.writeValueAsString(request),
        headers, HttpMethod.PUT, uri);

    restTemplate.exchange(requestEntity, String.class);

    return PAGE_PRE_FIX;
  }

  @PostMapping("/delete")
  public String deleteAuthor(@Valid @ModelAttribute DeleteByIdRequest request)
      throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    RequestEntity<String> requestEntity = new RequestEntity<>(
        objectMapper.writeValueAsString(request),
        headers, HttpMethod.DELETE, uri);

    restTemplate.exchange(requestEntity, String.class);
    return PAGE_PRE_FIX;
  }


  private ResponseEntity<PageResponse<RetrieveAuthorResponse>> retrieveAuthors(
      Optional<Integer> pageNum) {
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
        gatewayIp + SHOP_PRE_FIX + "?page=" + pageNum.get() + "&size=" + size);

    RequestEntity<PageResponse<RetrieveAuthorResponse>> requestEntity = new RequestEntity<>(
        httpHeaders, HttpMethod.GET, uri);

    return restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
    });

  }
}
