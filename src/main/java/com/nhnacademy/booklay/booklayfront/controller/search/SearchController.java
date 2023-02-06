package com.nhnacademy.booklay.booklayfront.controller.search;

import com.nhnacademy.booklay.booklayfront.controller.BaseController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchRequest;
import com.nhnacademy.booklay.booklayfront.service.search.SearchService;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController extends BaseController {

    private final SearchService searchService;
    private static final String PRODUCT_LIST_HTML = "product/display";

    @GetMapping
    public String defaultMapping() {
        return "redirect:/index";
    }

    @PostMapping
    public String searchResolve(@Valid @ModelAttribute SearchRequest searchRequest,
                                @RequestParam(value = "page", defaultValue = "0") int page,
                                Model model) {

        Optional<PageResponse<ProductAllInOneResponse>> response = searchService.getSearchResponse(searchRequest, page);

        if (response.isPresent()) {
            model.addAttribute("productList", response.get().getData());
            model.addAttribute("keywords", searchRequest.getKeywords());
            ControllerUtil.setCurrentPageAndMaxPageToModel(model, response.get());

            return PRODUCT_LIST_HTML;
        }
        return "redirect:/" + PRODUCT_LIST_HTML;
    }
}
