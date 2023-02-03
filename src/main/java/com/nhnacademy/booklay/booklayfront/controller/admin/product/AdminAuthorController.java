package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.DeleteByIdRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.author.request.CreateAuthorRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.author.request.UpdateAuthorRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.author.response.RetrieveAuthorResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.request.CreateDeleteProductRecommendRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  private final String gatewayIp;
  private final ObjectMapper objectMapper;
  private final RestService restService;


  //작가 관리창 조회
  @GetMapping()
  public String authorMaintenance(
      @RequestParam(value = "page", required = false) Optional<Integer> pageNum, Model model) {

    PageResponse<RetrieveAuthorResponse> authorPage = retrieveAuthors(pageNum);

    List<RetrieveAuthorResponse> authorList = authorPage.getData();

    setCurrentPageAndMaxPageToModel(model, authorPage);
    model.addAttribute("authorList", authorList);
    model.addAttribute(TARGET_VIEW, "product/adminAuthor");

    return "admin/adminPage";

  }

  //작가 팝업창
  @GetMapping("/popup")
  public String authorPopup(
      @RequestParam(value = "page", required = false) Optional<Integer> pageNum, Model model) {

    PageResponse<RetrieveAuthorResponse> authorPage = retrieveAuthors(pageNum);

    List<RetrieveAuthorResponse> authorList = authorPage.getData();

    setCurrentPageAndMaxPageToModel(model, authorPage);
    model.addAttribute("authorList", authorList);
    model.addAttribute(TARGET_VIEW, "product/adminAuthor");

    return "admin/product/popup/authorPopup";

  }

  //작가 생성
  @PostMapping
  public String createAuthor(@Valid @ModelAttribute CreateAuthorRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX);

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
        CreateDeleteProductRecommendRequest.class);

    return PAGE_PRE_FIX;

  }

  //작가 수정
  @PostMapping("/update")
  public String updateAuthor(@Valid @ModelAttribute UpdateAuthorRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX);

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
        CreateDeleteProductRecommendRequest.class);

    return PAGE_PRE_FIX;

  }

  //작가 삭제
  @PostMapping("/delete")
  public String deleteAuthor(@Valid @ModelAttribute DeleteByIdRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX);

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));
    return PAGE_PRE_FIX;

  }


  private PageResponse<RetrieveAuthorResponse> retrieveAuthors(
      Optional<Integer> pageNum) {
    if (pageNum.isEmpty()) {
      pageNum = Optional.of(0);
    }
    if (pageNum.get() < 0) {
      pageNum = Optional.of(0);
    }
    Long size = 20L;

    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX + "?page=" + pageNum.get() + "&size=" + size);

    ApiEntity<PageResponse<RetrieveAuthorResponse>> authorResponse = restService.get(
        uri.toString(), null, new ParameterizedTypeReference<>() {
        });

    return authorResponse.getBody();

  }
}