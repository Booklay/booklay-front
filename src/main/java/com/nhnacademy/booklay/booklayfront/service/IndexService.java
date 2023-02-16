package com.nhnacademy.booklay.booklayfront.service;

import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchProductResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Setter
@RequiredArgsConstructor
@Service
public class IndexService {

    private final String gatewayIp;
    private final RestService restService;
    private static final String LATEST_PRODUCTS = "/shop/v1/product/latest";

    public List<SearchProductResponse> getRecommendProducts() {
        URI uri = URI.create(gatewayIp + LATEST_PRODUCTS);

        ApiEntity<List<SearchProductResponse>> productResponse = restService.get(
            uri.toString(), null, new ParameterizedTypeReference<>() {});

        if (productResponse.isSuccess()) {
            return productResponse.getBody();
        }

        return List.of();
    }

}
