package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/tag/maintenance")
public class AdminTagController {

  private final RestTemplate restTemplate;
  private final String gatewayIp;

  @GetMapping()
  public String retrieveTag(
      @RequestParam(value = "page", required = false) Optional<Integer> pageNum,
      @RequestParam(value = "size", required = false) Optional<Integer> size, Model model) {
    if (pageNum.isEmpty()) {
      pageNum = Optional.of(0);
    }
    if (pageNum.get() < 0) {
      pageNum = Optional.of(0);
    }
    if (size.isEmpty()) {
      size = Optional.of(20);
    }

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

    URI uri = URI.create(
        gatewayIp + "/shop/v1/admin/product/tag?page=" + pageNum.get() + "&size=" + size.get());

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

  @PostMapping
  public String createTag(@Valid @ModelAttribute CreateTagRequest request)
      throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + "/shop/v1/admin/product/tag");

    log.info(request.getName());

    //1
    HttpHeaders headers = new HttpHeaders();
//    headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
    headers.setContentType(MediaType.APPLICATION_JSON);

    ObjectMapper mapper = new ObjectMapper();

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.POST, uri);

    log.info(requestEntity.getBody());

    restTemplate.exchange(requestEntity, RetrieveTagResponse.class);
    restTemplate.postForEntity(uri, request);

    return "redirect:";
  }

  @PutMapping
  public String updateTag(@Valid @ModelAttribute UpdateTagRequest request) {

    return "redirect:";
  }
}
