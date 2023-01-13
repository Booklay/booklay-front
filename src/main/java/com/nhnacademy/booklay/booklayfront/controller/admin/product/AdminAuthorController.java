package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateTagRequest;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/author/maintenance")
public class AdminAuthorController {

  private final RestTemplate restTemplate;
  private final String gatewayIp;


  @GetMapping()
  public String retrieveAuthor(
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
        gatewayIp + "/shop/v1/admin/product/author?page=" + pageNum.get() + "&size=" + size);

    RequestEntity<PageResponse<RetrieveAuthorResponse>> requestEntity = new RequestEntity<>(
        httpHeaders, HttpMethod.GET, uri);

    ResponseEntity<PageResponse<RetrieveAuthorResponse>> testAuthors =
        restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
        });

    int totalPage = testAuthors.getBody().getTotalPages();
    int nowPage = testAuthors.getBody().getPageNumber();
    List<RetrieveAuthorResponse> authorList = testAuthors.getBody().getData();

    model.addAttribute("nowPage", nowPage);
    model.addAttribute("totalPage", totalPage);
    model.addAttribute("authorList", authorList);

    return "/admin/product/adminAuthor";
  }

  @PostMapping
  public String createAuthor(@Valid @ModelAttribute CreateAuthorRequest request)
      throws JsonProcessingException {
    URI uri = URI.create(gatewayIp + "/shop/v1/admin/product/author");

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    ObjectMapper mapper = new ObjectMapper();

    RequestEntity<String> requestEntity = new RequestEntity<>(mapper.writeValueAsString(request),
        headers, HttpMethod.POST, uri);

    log.info(requestEntity.getBody());

    restTemplate.exchange(requestEntity, RetrieveTagResponse.class);

    return "redirect:/admin/product/author/maintenance";
  }
}
