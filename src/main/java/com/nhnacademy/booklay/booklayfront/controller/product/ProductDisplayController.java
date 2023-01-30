package com.nhnacademy.booklay.booklayfront.controller.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductViewResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.wishlist.request.CreateWishlistRequest;
import java.net.URI;
import java.util.List;
import java.util.Objects;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

/**
 * @author 최규태
 */

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductDisplayController {

    private final static String SHOP_PRE_FIX = "/shop/v1/product";
    private final String gatewayIp;
    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    @GetMapping("/board")
    public String retrieveProduct(
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
            gatewayIp + SHOP_PRE_FIX + "?page=" + pageNum.get() + "&size=" + size);

        RequestEntity<PageResponse<RetrieveProductResponse>> requestEntity = new RequestEntity<>(
            httpHeaders, HttpMethod.GET, uri);

        ResponseEntity<PageResponse<RetrieveProductResponse>> productResponse =
            restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {
            });

        if (Objects.nonNull(productResponse.getBody())) {

            int totalPage = productResponse.getBody().getTotalPages();
            int nowPage = productResponse.getBody().getPageNumber();
            List<RetrieveProductResponse> productList = productResponse.getBody().getData();

            model.addAttribute("nowPage", nowPage);
            model.addAttribute("totalPage", totalPage);
            model.addAttribute("productList", productList);
        }

        return "product/productBoard";
    }

    @GetMapping("/view/{productNo}")
    public String productViewer(@PathVariable("productNo") Long productNo, Model model)
        throws JsonProcessingException {
        URI uri = URI.create(gatewayIp + SHOP_PRE_FIX + "/view/" + productNo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<String> requestEntity = new RequestEntity<>(null,
            headers, HttpMethod.GET, uri);


        ResponseEntity<RetrieveProductViewResponse> response =
            restTemplate.exchange(requestEntity, RetrieveProductViewResponse.class);

        model.addAttribute("productNo", productNo);
        model.addAttribute("product", response.getBody());

        return "/product/productDetailView";
    }

    @PostMapping("/view/wishlist/register")
    public String wishlistRegister(@Valid @ModelAttribute CreateWishlistRequest request)
        throws JsonProcessingException {

        URI uri = URI.create(gatewayIp + "/shop/v1/mypage/wishlist/");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity<String> requestEntity =
            new RequestEntity<>(mapper.writeValueAsString(request),
                headers, HttpMethod.POST, uri);

        restTemplate.exchange(requestEntity, String.class);

        return "redirect:/product/view/" + request.getProductNo();
    }

    @GetMapping("/display")
    public String categoryViewer(
        @RequestParam(name = "CID", defaultValue = "1") List<Long> categoryId,
        @RequestParam(name = "page", defaultValue = "0") Long pageNum,
        Model model) {

//        TODO SHOP 컨트롤러 매핑 후 주석 해제

        /*

                  ApiEntity<PageResponse<RetrieveProductResponse>> response =
                      restService.get(gatewayIp + SHOP_PRE_FIX + "/categories?CID=" + categoryId.toString() + "&page=" + pageNum, null, new ParameterizedTypeReference<>() {});
          <p>
                  PageResponse<RetrieveProductResponse> responseBody = response.getBody();
          <p>
                  model.addAttribute("nowPage", responseBody.getPageNumber());
                  model.addAttribute("totalPage", responseBody.getTotalPages());
                  model.addAttribute("productList", responseBody.getData());
         */


        return "redirect:/product/board";
    }
}
