package com.nhnacademy.booklay.booklayfront.controller.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

@Controller
@RequiredArgsConstructor
@RequestMapping("/storage")
public class ObjectFileStorageController {

  private final ObjectMapper objectMapper;
  private final RestTemplate restTemplate;
  private final String gatewayIp;

  @GetMapping("/{objectFileId}")
  public String downloadFile(@PathVariable Long objectFileId) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));

    URI uri = URI.create(gatewayIp + "/shop/v1/storage/" + objectFileId);

    RequestEntity<PageResponse<RetrieveProductResponse>> requestEntity = new RequestEntity<>(
        headers, HttpMethod.GET, uri);

    ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);


    return "redirect:" +response.getBody();
  }
}
