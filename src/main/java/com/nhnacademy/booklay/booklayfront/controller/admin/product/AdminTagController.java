package com.nhnacademy.booklay.booklayfront.controller.admin.product;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.TARGET_VIEW;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.product.DeleteByIdRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateDeleteTagProductRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.CreateTagRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.request.UpdateTagRequest;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.RetrieveTagResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.tag.response.TagProductResponse;
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
@RequestMapping("/admin/product/tag")
public class AdminTagController {

  private static final String MAINTENANCE_PRE_FIX = "redirect:/admin/product/tag/maintenance";
  private static final String CONNECTION_PRE_FIX = "redirect:/admin/product/tag/connection";
  private static final String SHOP_URI_PRE_FIX = "/shop/v1/admin/tag";
  private static final Integer SIZE = 20;
  private final String gatewayIp;
  private final ObjectMapper objectMapper;
  private final RestService restService;

  /**
   * 페이지 조회
   */
  @GetMapping("/maintenance")
  public String retrieveTag(
      @RequestParam(value = "page", defaultValue = "0") int page, Model model) {

    URI uri = URI.create(
        gatewayIp + SHOP_URI_PRE_FIX);

    ApiEntity<PageResponse<RetrieveTagResponse>> tagResponse = restService.get(
        uri.toString(), getDefaultPageMap(page,SIZE), new ParameterizedTypeReference<>() {
        });

    if (tagResponse.isSuccess()) {
      List<RetrieveTagResponse> tagList = tagResponse.getBody().getData();

      setCurrentPageAndMaxPageToModel(model, tagResponse.getBody());
      model.addAttribute("tagList", tagList);
      model.addAttribute(TARGET_VIEW, "product/adminTag");

      return "admin/product/tag/maintenance";
    }
    return "/index";
  }

  //생성
  @PostMapping("/maintenance/{pageNum}")
  public String createTagAtMaintenance(@Valid @ModelAttribute CreateTagRequest request,
      @PathVariable Long pageNum) {
    createTag(request);

    log.info("페이지 번호 출력 : " + pageNum);
    return MAINTENANCE_PRE_FIX + "?page=" + pageNum + "&size=" + SIZE;
  }

  //수정
  @PostMapping("/maintenance/update/{pageNum}")
  public String updateTagAtMaintenance(@Valid @ModelAttribute UpdateTagRequest request,
      @PathVariable Long pageNum) {
    updateTag(request);

    return MAINTENANCE_PRE_FIX + "?page=" + pageNum + "&size=" + SIZE;
  }

  //삭제
  @PostMapping("/maintenance/delete/{pageNum}")
  public String deleteTagAtMaintenance(@Valid @ModelAttribute DeleteByIdRequest request,
      @PathVariable Long pageNum) {
    deleteTag(request);
    return MAINTENANCE_PRE_FIX + "?page=" + pageNum + "&size=" + SIZE;
  }

  /**
   * 태그 등록 페이지 조회
   */
  @GetMapping("/connection/{productNo}")
  public String retrieveTagForProductConnect(@PathVariable("productNo") Long productNo,
      @RequestParam(value = "page", defaultValue = "0") int page, Model model) {

    Long size = 10L;

    URI uri = URI.create(
        gatewayIp + "/shop/v1/admin/tag/product/" + productNo);

    ApiEntity<PageResponse<TagProductResponse>> tagResponse = restService.get(
        uri.toString(), getDefaultPageMap(page,SIZE), new ParameterizedTypeReference<>() {
        });

    List<TagProductResponse> tagList = tagResponse.getBody().getData();

    setCurrentPageAndMaxPageToModel(model, tagResponse.getBody());
    model.addAttribute("tagList", tagList);
    model.addAttribute("productNo", productNo);

    return "admin/product/tag/connector";
  }

  /**
   * 상품에 태그 등록
   */
  @PostMapping("/connection/{productNo}/{pageNum}")
  public String tagProductConnect(@PathVariable("productNo") Long productNo,
      @PathVariable Long pageNum, @Valid @ModelAttribute CreateDeleteTagProductRequest request) {

    URI uri = URI.create(gatewayIp + "/shop/v1/admin/tag/product");

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class),
        CreateDeleteTagProductRequest.class);

    return CONNECTION_PRE_FIX + "/" + productNo + "?page=" + pageNum + "&size=" + SIZE;
  }

  /**
   * 상품에 등록된 태그 삭제
   */
  @PostMapping("/connection/disconnect/{productNo}/{pageNum}")
  public String tagProductDisconnect(
      @PathVariable("productNo") Long productNo, @PathVariable Long pageNum,
      @Valid @ModelAttribute CreateDeleteTagProductRequest request) {

    URI uri = URI.create(gatewayIp + "/shop/v1/admin/tag/product");

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));

    return CONNECTION_PRE_FIX + "/" + productNo + "?page=" + pageNum + "&size=" + SIZE;
  }

  /**
   * 연결창에서 태그 생성
   */
  @PostMapping("/connection/create/{productNo}/{pageNum}")
  public String createTagAtConnector(@Valid @ModelAttribute CreateTagRequest request,
      @PathVariable Long pageNum, @PathVariable Long productNo) {
    createTag(request);
    return CONNECTION_PRE_FIX + "/" + productNo + "?page=" + pageNum + "&size=" + SIZE;
  }

  /**
   * 연결창에서 태그 수정
   */
  @PostMapping("/connection/update/{productNo}/{pageNum}")
  public String updateTagAtConnector(@Valid @ModelAttribute UpdateTagRequest request,
      @PathVariable Long pageNum, @PathVariable Long productNo) {

    updateTag(request);
    return CONNECTION_PRE_FIX + "/" + productNo + "?page=" + pageNum + "&size=" + SIZE;
  }

  /**
   * 연결창에서 태그 삭제
   */
  @PostMapping("/connection/delete/{productNo}/{pageNum}")
  public String deleteTagAtConnector(@PathVariable Long pageNum, @PathVariable Long productNo,
      @Valid @ModelAttribute DeleteByIdRequest request) {
    deleteTag(request);
    return CONNECTION_PRE_FIX + "/" + productNo + "?page=" + pageNum + "&size=" + SIZE;
  }

  //공통 부분 리팩토링

  /**
   * 태그 생성
   */
  public void createTag(CreateTagRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX);

    restService.post(uri.toString(), objectMapper.convertValue(request, Map.class), String.class);
  }

  /**
   * 태그 수정
   */
  public void updateTag(UpdateTagRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX);

    restService.put(uri.toString(), objectMapper.convertValue(request, Map.class), String.class);
  }

  /**
   * 태그 삭제
   */
  public void deleteTag(DeleteByIdRequest request) {
    URI uri = URI.create(gatewayIp + SHOP_URI_PRE_FIX);

    restService.delete(uri.toString(), objectMapper.convertValue(request, Map.class));
  }
}
