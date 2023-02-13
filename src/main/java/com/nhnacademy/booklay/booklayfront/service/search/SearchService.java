package com.nhnacademy.booklay.booklayfront.service.search;

import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchIdRequest;
import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchKeywordsRequest;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchProductResponse;
import java.util.Optional;

public interface SearchService {
    Optional<SearchPageResponse<SearchProductResponse>> getSearchResponse(
        SearchKeywordsRequest searchKeywordsRequest, int page);
    Optional<SearchPageResponse<SearchProductResponse>> getSearchResponse(int page);
    Optional<SearchPageResponse<SearchProductResponse>> getSearchResponseByNestedId(
        SearchIdRequest searchIdRequest, int page);
}
