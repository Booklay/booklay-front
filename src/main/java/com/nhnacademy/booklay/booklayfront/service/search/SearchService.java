package com.nhnacademy.booklay.booklayfront.service.search;

import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.ProductAllInOneResponse;
import com.nhnacademy.booklay.booklayfront.dto.product.product.response.RetrieveProductResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.request.SearchRequest;
import java.util.Optional;

public interface SearchService {
    Optional<PageResponse<ProductAllInOneResponse>> getSearchResponse(SearchRequest searchRequest, int page);
}
