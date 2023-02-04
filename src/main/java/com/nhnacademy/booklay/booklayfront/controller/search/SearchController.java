package com.nhnacademy.booklay.booklayfront.controller.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.net.URI;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController extends BaseController {

    private static final String PRODUCT_LIST_HTML = "product/display";
    private static final String REQUEST_SEARCH_PRODUCT_URI = "/shop/v1/search/products";
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;

    public SearchController(RestService restService, ObjectMapper objectMapper, String gatewayIp) {
        this.restService = restService;
        this.objectMapper = objectMapper;
        this.gatewayIp = gatewayIp;
    }

    @GetMapping
    public String defaultMapping() {
        return "redirect:/index";
    }

    @PostMapping
    public String searchByKeyword(@Valid @ModelAttribute SearchRequest searchRequest,
                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                  Model model) {

        String query = "?page=" + page;

        URI uri = URI.create(
            gatewayIp + REQUEST_SEARCH_PRODUCT_URI + "/" + searchRequest.getClassification() + query);

        // 리팩토링 시 인자에 작성된 제네릭 타입 지우지 말 것, 요청에 대한 response 타입이 제대로 적용되지 않음.
        ApiEntity<PageResponse<RetrieveProductResponse>> response = restService.post(uri.toString(), objectMapper.convertValue(searchRequest, Map.class), new ParameterizedTypeReference<PageResponse<RetrieveProductResponse>>(){});

        if (response.isSuccess()) {
            model.addAttribute("productList", response.getBody().getData());
            model.addAttribute("totalPage", response.getBody().getTotalPages());
            model.addAttribute("nowPage", response.getBody().getPageNumber());
            return PRODUCT_LIST_HTML;
        }
        return "redirect: product/display";
    }
}
