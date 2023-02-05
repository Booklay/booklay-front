package com.nhnacademy.booklay.booklayfront.controller.admin.category;

import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.category.request.CategoryCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.category.request.CategoryUpdateRequest;
import com.nhnacademy.booklay.booklayfront.dto.category.response.CategoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {

  private final String redirectGatewayPrefix;
  private final RestService restService;
  private final ObjectMapper objectMapper;

  public CategoryAdminController(RestService restService,
      String gateway, ObjectMapper objectMapper) {
    this.redirectGatewayPrefix = gateway + "/shop/v1" + "/admin/categories";
    this.restService = restService;
    this.objectMapper = objectMapper;
  }

  private static final String REDIRECT_PREFIX = "redirect:/admin/categories";

  /**
   * 카테고리 등록.
   *
   * @param createRequest .
   * @param model         .
   * @return .
   */
  @PostMapping
  public String createCategory(@Valid @ModelAttribute CategoryCreateRequest createRequest,
      Model model) {

    URI uri = URI.create(redirectGatewayPrefix);

    ApiEntity<CategoryResponse> response =
        restService.post(uri.toString(), objectMapper.convertValue(createRequest, Map.class),
            CategoryResponse.class);

    model.addAttribute("category", response.getBody());

    return REDIRECT_PREFIX;
  }

  /**
   * 카테고리 등록 화면.
   *
   * @return .
   */
  @GetMapping("/create")
  public String createForm() {
    return "admin/category/createForm";
  }

  /**
   * 카테고리 수정 화면.
   *
   * @param id    .
   * @param model .
   * @return .
   */
  @GetMapping("/{categoryId}/update")
  public String updateForm(@PathVariable(name = "categoryId") Long id, Model model) {
    URI uri = URI.create(redirectGatewayPrefix + "/" + id);

    ApiEntity<CategoryResponse> categoryResponse =
        restService.get(uri.toString(), null, CategoryResponse.class);

    if (categoryResponse.isSuccess()) {
      model.addAttribute("category", categoryResponse.getBody());
      return "admin/category/updateForm";
    } else {
      return REDIRECT_PREFIX;
    }
  }


  /**
   * 카테고리 리스트 화면.
   *
   * @param page .
   * @param model  .
   * @return .
   */
  @GetMapping
  public String retrieveCategoryList(@RequestParam(value = "page", defaultValue = "0") int page,
      Model model) {

    String query = "?page=" + page;

    URI uri = URI.create(redirectGatewayPrefix + query);

    ApiEntity<PageResponse<CategoryResponse>> response =
        restService.get(uri.toString(), null, new ParameterizedTypeReference<>() {
        });

    if (response.isSuccess()) {

      setCurrentPageAndMaxPageToModel(model, response.getBody());
      model.addAttribute("list", response.getBody().getData());
      return "admin/category/list";
    } else {
      return "/index";
    }
  }

  /**
   * 카테고리 수정.
   *
   * @param request .
   * @param id      .
   * @return .
   */
  @PutMapping("/{categoryId}")
  public String updateCategory(@Valid @ModelAttribute CategoryUpdateRequest request,
      @PathVariable("categoryId") Long id) {

    URI uri = URI.create(redirectGatewayPrefix + "/" + id);

    ApiEntity<CategoryResponse> categoryResponse =
        restService.put(uri.toString(), objectMapper.convertValue(request, Map.class),
            CategoryResponse.class);

    if (categoryResponse.isSuccess()) {
      return REDIRECT_PREFIX;
    }

    return REDIRECT_PREFIX + "/" + id + "/update";
  }


  /**
   * 카테고리 삭제.
   *
   * @param id .
   * @return .
   */
  @DeleteMapping("/{categoryId}")
  public String deleteCategory(@PathVariable("categoryId") Long id,
      @RequestParam(value = "page", defaultValue = "0") int page) {

    String query = "?page=" + page;

    URI uri = URI.create(redirectGatewayPrefix + "/" + id);

    restService.delete(uri.toString());

    return REDIRECT_PREFIX + query;
  }

}
