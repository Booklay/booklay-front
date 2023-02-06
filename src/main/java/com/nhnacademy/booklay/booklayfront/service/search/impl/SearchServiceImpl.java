package com.nhnacademy.booklay.booklayfront.service.search.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchRequest;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.search.SearchService;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * 검색 창 결과를 토대로 상품 노출 필터링.
 */
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;
    private static final String REQUEST_SEARCH_PRODUCT_URI = "/shop/v1/search/products";

    /**
     * 검색결과를 페이지네이션과 함께 리턴.
     *
     * @param searchRequest classification 검색 분류 옵션 "keywords" "tags" "authors" "category" API 로 가는 URI 를 결정하는 부분
     * @param page 페이지네이션 정보
     * @return Optional 을 이용해 컨트롤러에서 분기가 가능하도록 처리
     */
    @Override
    public Optional<PageResponse<ProductAllInOneResponse>> getSearchResponse(SearchRequest searchRequest, int page) {

        String query = "?page=" + page;

        URI uri = URI.create(gatewayIp + REQUEST_SEARCH_PRODUCT_URI + query);

        // 리팩토링 시 인자에 작성된 제네릭 타입 지우지 말 것, 요청에 대한 response 타입이 제대로 적용되지 않음.
        ApiEntity<PageResponse<ProductAllInOneResponse>> response = restService.post(uri.toString(), objectMapper.convertValue(searchRequest, Map.class), new ParameterizedTypeReference<PageResponse<ProductAllInOneResponse>>(){});

        if (response.isSuccess()) {
            return Optional.of(response.getBody());
        }

        return Optional.empty();
    }
}

