package com.nhnacademy.booklay.booklayfront.service.search.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchProductResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.search.SearchService;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

/**
 * 검색 창 결과를 토대로 상품 노출 필터링.
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;
    private static final String REQUEST_SEARCH_PRODUCT_URI = "/shop/v1/search/products/keywords";
    private static final String REQUEST_SEARCH_BY_PRODUCT_NESTED_URI = "/shop/v1/search/products";
    private static final String REQUEST_PRODUCT_ALL = "/shop/v1/product/all";

    /**
     * 전체 상품 노출.
     */
    @Override
    public Optional<SearchPageResponse<SearchProductResponse>> getSearchResponse(int page) {

        URI uri = URI.create(gatewayIp + REQUEST_PRODUCT_ALL);

        // 리팩토링 시 인자에 작성된 제네릭 타입 지우지 말 것, 요청에 대한 response 타입이 제대로 적용되지 않음.

        log.info(" Search All Products");


        ApiEntity<SearchPageResponse<SearchProductResponse>> response =
            restService.get(uri.toString(), ControllerUtil.getDefaultPageMap(page),
                new ParameterizedTypeReference<SearchPageResponse<SearchProductResponse>>() {
                });

        if (response.isSuccess()) {
            return Optional.of(response.getBody());
        }

        return Optional.empty();
    }

    /**
     * 검색결과를 페이지네이션과 함께 리턴.
     *
     * @param searchKeywordsRequest classification 검색 분류 옵션 "keywords" "tags" "authors" "category" API 로 가는 URI 를 결정하는 부분
     * @return Optional 을 이용해 컨트롤러에서 분기가 가능하도록 처리
     */
    @Override
    public Optional<SearchPageResponse<SearchProductResponse>> getSearchResponse(
        SearchKeywordsRequest searchKeywordsRequest, int page) {

        URI uri = URI.create(gatewayIp + REQUEST_SEARCH_PRODUCT_URI);

        log.info(" \n Search Classification :  {} \n Keywords : {}",
            searchKeywordsRequest.getClassification(), searchKeywordsRequest.getKeywords());


        // 리팩토링 시 인자에 작성된 제네릭 타입 지우지 말 것, 요청에 대한 response 타입이 제대로 적용되지 않음.

        ApiEntity<SearchPageResponse<SearchProductResponse>> response =
            restService.post(
                uri.toString(), objectMapper.convertValue(searchKeywordsRequest, Map.class),
                ControllerUtil.getDefaultPageMap(page),
                new ParameterizedTypeReference<SearchPageResponse<SearchProductResponse>>(){});

        if (response.isSuccess()) {
            return Optional.of(response.getBody());
        }

        return Optional.empty();
    }

    @Override
    public Optional<SearchPageResponse<SearchProductResponse>> getSearchResponseByNestedId(
        SearchIdRequest searchIdRequest, int page) {

        log.info(" \n Search Classification :  {} \n ID : {}",
            searchIdRequest.getClassification(), searchIdRequest.getId());

        URI uri = URI.create(gatewayIp + REQUEST_SEARCH_BY_PRODUCT_NESTED_URI);

        // 리팩토링 시 인자에 작성된 제네릭 타입 지우지 말 것, 요청에 대한 response 타입이 제대로 적용되지 않음.
        ApiEntity<SearchPageResponse<SearchProductResponse>> response =
            restService.post(
                uri.toString(), objectMapper.convertValue(searchIdRequest, Map.class),
                ControllerUtil.getDefaultPageMap(page),
                new ParameterizedTypeReference<SearchPageResponse<SearchProductResponse>>(){});

        if (response.isSuccess()) {
            return Optional.of(response.getBody());
        }

        return Optional.empty();
    }

}

