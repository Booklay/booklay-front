package com.nhnacademy.booklay.booklayfront.controller.admin.category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
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
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final RestTemplate restTemplate;
    private final String redirectGatewayPrefix;
    private static final String REDIRECT_PREFIX = "redirect:/admin/categories";

    public CategoryAdminController(RestTemplate restTemplate, String gateway) {
        this.restTemplate = restTemplate;
        redirectGatewayPrefix = gateway + "/shop/v1" + "/admin/categories";
    }

    @PostMapping
    public String createForm(@Valid @ModelAttribute CategoryCreateRequest createRequest,
                             Model model) throws JsonProcessingException {
        URI uri = URI.create(redirectGatewayPrefix);

        ObjectMapper objectMapper = new ObjectMapper();

        log.info(" test " + objectMapper.writeValueAsString(createRequest));


        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);


        RequestEntity<String> requestEntity =
            new RequestEntity<>(objectMapper.writeValueAsString(createRequest), httpHeaders,
                HttpMethod.POST, uri);

        log.info("Front Category Create");

        ResponseEntity<CategoryResponse> response =
            restTemplate.exchange(requestEntity, CategoryResponse.class);

        log.info(" test " + objectMapper.writeValueAsString(response.getBody()));

        model.addAttribute("category", response.getBody());

        return "category/detail";
    }

    @GetMapping("/create")
    public String createForm() {
        return "category/createForm";
    }


    @GetMapping("/test")
    public String test(Model model) {
        URI uri = URI.create(redirectGatewayPrefix + "/test");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        RequestEntity<CategoryCreateRequest> requestEntity =
            new RequestEntity<>(httpHeaders, HttpMethod.POST, uri);


        ResponseEntity<CategoryResponse> response =
            restTemplate.exchange(requestEntity, CategoryResponse.class);

        CategoryResponse categoryResponse = response.getBody();

        model.addAttribute("category", categoryResponse);

        return "category/list";
    }

    @GetMapping
    public String retrieveCategoryList(
        @RequestParam(value = "page", defaultValue = "0") int page,
        Model model) {

        String query = "?page=" + page;

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        URI uri = URI.create(redirectGatewayPrefix + query);

        RequestEntity<PageResponse<CategoryResponse>> requestEntity =
            new RequestEntity<>(httpHeaders, HttpMethod.GET, uri);

        ResponseEntity<PageResponse<CategoryResponse>> response =
            restTemplate.exchange(requestEntity,
                new ParameterizedTypeReference<>() {
                });

        List<CategoryResponse> list = Objects.requireNonNull(response.getBody()).getData();

        model.addAttribute("categories", list);

        return "category/list";
    }

    @GetMapping("/{categoryId}")
    public String deleteCategory(@PathVariable("categoryId") Long id) {
        restTemplate.delete(redirectGatewayPrefix + "/" + id);

        return REDIRECT_PREFIX;
    }
}
