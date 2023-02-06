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
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author 최규태
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/product/author")
public class AdminAuthorController {

  private static final String MAINTENANCE = "redirect:/admin/product/author/maintenance";
  private static final String POPUP = "redirect:/admin/product/author/popup";
  private static final String SHOP_PRE_FIX = "/shop/v1/";
  private static final String AUTHOR_PRE_FIX = "admin/author";
  private static final Long SIZE = 20L;
  private final String gatewayIp;
  private final ObjectMapper objectMapper;
  private final RestService restService;


  /**
   * 작가 관리창 조회
   * @param page
   * @param model
   * @return
   */
  @GetMapping("/maintenance")
  public String authorMaintenance(
      @RequestParam(value = "page", defaultValue = "0") int page, Model model) {

    PageResponse<RetrieveAuthorResponse> authorPage = retrieveAuthors(page);

    List<RetrieveAuthorResponse> authorList = authorPage.getData();

    setCurrentPageAndMaxPageToModel(model, authorPage);
    model.addAttribute("authorList", authorList);
    model.addAttribute(TARGET_VIEW, "product/adminAuthor");

    return "admin/product/author/maintenance";

  }

  /**
   * 작가 팝업창
   * @param page
   * @param model
   * @return
   */
  @GetMapping("/popup")
  public String authorPopup(
      @RequestParam(value = "page", defaultValue = "0") int page, Model model) {

    PageResponse<RetrieveAuthorResponse> authorPage = retrieveAuthors(page);

    List<RetrieveAuthorResponse> authorList = authorPage.getData();

    setCurrentPageAndMaxPageToModel(model, authorPage);
    model.addAttribute("authorList", authorList);
    model.addAttribute(TARGET_VIEW, "product/adminAuthor");

    return "admin/product/author/popup";

  }

  /**
   * 작가 생성
   * @param request
   * @param pageNum
   * @return
   */
  @PostMapping("/maintenance/{pageNum}")
  public String createAuthor(@Valid @ModelAttribute CreateAuthorRequest request,
      @PathVariable String pageNum) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + AUTHOR_PRE_FIX);

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
        CreateDeleteProductRecommendRequest.class);

    return MAINTENANCE + "?page=" + pageNum + "&size=" + SIZE;

  }

  /**
   * 작가 삭제
   * @param request
   * @param pageNum
   * @return
   */
  @PostMapping("/maintenance/delete/{pageNum}")
  public String deleteAuthor(@Valid @ModelAttribute DeleteByIdRequest request,
      @PathVariable Long pageNum) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + AUTHOR_PRE_FIX);

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));
    return MAINTENANCE + "?page=" + pageNum + "&size=" + SIZE;

  }

  /**
   * 작가 생성
   * @param request
   * @param pageNum
   * @return
   */
  @PostMapping("/popup/{pageNum}")
  public String createAuthorFromPopup(@Valid @ModelAttribute CreateAuthorRequest request,
      @PathVariable String pageNum) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + AUTHOR_PRE_FIX);

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
        CreateDeleteProductRecommendRequest.class);

    return POPUP + "?page=" + pageNum + "&size=" + SIZE;

  }

  /**
   * 작가 삭제
   * @param request
   * @param pageNum
   * @return
   */
  @PostMapping("/popup/delete/{pageNum}")
  public String deleteAuthorFromPopup(@Valid @ModelAttribute DeleteByIdRequest request,
      @PathVariable Long pageNum) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + AUTHOR_PRE_FIX);

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));
    return POPUP + "?page=" + pageNum + "&size=" + SIZE;

  }

  /**
   * 작가 수정 팝업 조회
   * @param authorNo
   * @return
   */
  @GetMapping("/edit/{authorNo}")
  public String getAuthorEditPopup(@PathVariable String authorNo, Model model){
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + AUTHOR_PRE_FIX + "/" + authorNo);

    ApiEntity<RetrieveAuthorResponse> author = restService.get(uri.toString(), null,
        RetrieveAuthorResponse.class);

    model.addAttribute("author", author.getBody());
    return "admin/product/author/edit";
  }


  /**
   * 작가 수정 요청
   * @param request
   * @return
   */
  @PostMapping()
  public void updateAuthor(@Valid @ModelAttribute UpdateAuthorRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + AUTHOR_PRE_FIX);

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
        CreateDeleteProductRecommendRequest.class);
  }

  /**
   * 작가 페이지 조회 rest Service
   * @param page
   * @return
   */
  private PageResponse<RetrieveAuthorResponse> retrieveAuthors(
      int page) {
    URI uri = URI.create(
        gatewayIp + SHOP_PRE_FIX + AUTHOR_PRE_FIX + "?page=" + page + "&size=" + SIZE);

    ApiEntity<PageResponse<RetrieveAuthorResponse>> authorResponse = restService.get(
        uri.toString(), null, new ParameterizedTypeReference<>() {
        });

    return authorResponse.getBody();

  }
}