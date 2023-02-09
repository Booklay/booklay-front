package com.nhnacademy.booklay.booklayfront.controller.search;

import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.booklayfront.service.search.SearchService;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController extends BaseController {

    private final SearchService searchService;
    private static final String PRODUCT_LIST_HTML = "product/displayV2";
    private static final String PRODUCT_LIST = "productList";

    private static final String SEARCH_TITLE = "검색 : ";
    private static final String ATTR_BODY_TITLE = "bodyTitle";

    @GetMapping("/**")
    public String defaultMapping(HttpServletRequest request) {

        return "redirect:/index";
    }

    @GetMapping("/products/all")
    public String searchResolve(@RequestParam(value = "page", defaultValue = "0") int page, Model model) {

        Optional<SearchPageResponse<SearchProductResponse>> response = searchService.getSearchResponse(page);

        if (response.isPresent()) {
            model.addAttribute(PRODUCT_LIST, response.get().getData());
            model.addAttribute(ATTR_BODY_TITLE, getBodyTitle(response.get()));
            log.info(" Search Product ALL");

            ControllerUtil.setCurrentPageAndMaxPageToModel(model, response.get());

        } else {
            model.addAttribute(PRODUCT_LIST, List.of());
        }


        return PRODUCT_LIST_HTML;
    }

    private String getBodyTitle(SearchPageResponse<SearchProductResponse> response) {
        return  SEARCH_TITLE + response.getSearchKeywords();
    }

    @GetMapping("/products/category")
    public String searchByCategoryId(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(required = false) Long id,
                                     Model model) {

        Optional<SearchPageResponse<SearchProductResponse>> response = searchService.getSearchResponseByCategory(id, page);

        if (response.isPresent()) {
            model.addAttribute(PRODUCT_LIST, response.get().getData());
            model.addAttribute(ATTR_BODY_TITLE, getBodyTitle(response.get()));
            log.info(" Search Product ALL");

            ControllerUtil.setCurrentPageAndMaxPageToModel(model, response.get());

        } else {
            model.addAttribute(PRODUCT_LIST, List.of());
        }

        return PRODUCT_LIST_HTML;

    }

    /**
     * 검색창에서 키워드 검색 처리.
     *
     * @param searchKeywordsRequest 검색 분류와 검색 키워드.
     * @param page 페이지네이션 정보.
     *
     */
    @PostMapping("/products")
    public String searchResolve(@Valid @ModelAttribute SearchKeywordsRequest searchKeywordsRequest,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                Model model) {

        Optional<SearchPageResponse<SearchProductResponse>> response = searchService.getSearchResponse(
            searchKeywordsRequest, page);

        if (response.isPresent()) {
            model.addAttribute(PRODUCT_LIST, response.get().getData());
            model.addAttribute("keywords", searchKeywordsRequest.getKeywords());
            model.addAttribute("classification", searchKeywordsRequest.getClassification());

            model.addAttribute(ATTR_BODY_TITLE, getBodyTitle(response.get()));
            log.info(" Search Product, keywords : {}, classification : {}", searchKeywordsRequest.getKeywords(), searchKeywordsRequest.getClassification());

            ControllerUtil.setCurrentPageAndMaxPageToModel(model, response.get());

        } else {
            model.addAttribute(PRODUCT_LIST, List.of());
        }

        return PRODUCT_LIST_HTML;
    }
}
