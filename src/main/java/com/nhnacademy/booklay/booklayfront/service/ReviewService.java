package com.nhnacademy.booklay.booklayfront.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.review.request.ReviewCreateRequest;
import com.nhnacademy.booklay.booklayfront.dto.review.response.RetrieveReviewResponse;
import com.nhnacademy.booklay.booklayfront.dto.search.response.SearchPageResponse;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final RestService restService;
    private final RestTemplate restTemplate;
    private final String gatewayIp;
    private final ObjectMapper objectMapper;

    private static final String REVIEW_CREATE_URI = "/shop/v1/reviews";
    private static final String REVIEW_RETRIEVE_BY_PRODUCT_URI = "/shop/v1/reviews/products/";
    private static final String REVIEW_DELETE_BY_REVIEW_ID_URI = "/shop/v1/reviews/";

    public HttpStatus registerReview(ReviewCreateRequest request, MultipartFile file)
        throws IOException {

        ByteArrayResource contentsAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {return file.getOriginalFilename();}};

        MultipartBodyBuilder resource = new MultipartBodyBuilder();
        resource.part("request", objectMapper.writeValueAsString(request), MediaType.APPLICATION_JSON);
        resource.part("file", contentsAsResource, MediaType.valueOf(Objects.requireNonNull(file.getContentType())));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();
        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity =
            new HttpEntity<>(multipartBody, headers);

        ResponseEntity<Void> responseEntity =
            restTemplate.postForEntity(gatewayIp + REVIEW_CREATE_URI, httpEntity, Void.class);

        return responseEntity.getStatusCode();
    }

    public SearchPageResponse<RetrieveReviewResponse> retrieveReview(Long postId, int page) {

        ApiEntity<SearchPageResponse<RetrieveReviewResponse>> response = restService.get(
            gatewayIp + REVIEW_RETRIEVE_BY_PRODUCT_URI + postId,
            ControllerUtil.getDefaultPageMap(page, 5),
            new ParameterizedTypeReference<SearchPageResponse<RetrieveReviewResponse>>(){});

        if (response.isSuccess()) {
            return response.getBody();
        }

        return null;

    }

    public void deleteReviewById(Long reviewId) {

        restService.delete(gatewayIp + REVIEW_DELETE_BY_REVIEW_ID_URI + reviewId);

    }
}
