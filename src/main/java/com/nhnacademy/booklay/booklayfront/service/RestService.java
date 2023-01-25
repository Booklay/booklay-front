package com.nhnacademy.booklay.booklayfront.service;


import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Service
public class RestService {


    public <T> ApiEntity<T> post(String url, Map<String, Object> requestBody,
                                 Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
        apiEntity.setSuccessResponse(response);
        return apiEntity;
    }

    public <T> ApiEntity<T> put(String url, Map<String, Object> requestBody,
                                Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
        apiEntity.setSuccessResponse(response);
        return apiEntity;
    }

    public <T> ApiEntity<T> get(String url, MultiValueMap<String, String> params,
                                ParameterizedTypeReference<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, null,
                responseType);
        apiEntity.setSuccessResponse(response);
        return apiEntity;
    }

    public <T> ApiEntity<T> get(String url, MultiValueMap<String, String> params,
                                Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);
        ApiEntity<T> apiEntity = new ApiEntity<>();
        ResponseEntity<T> response =
            restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, null,
                responseType);
        apiEntity.setSuccessResponse(response);

        return apiEntity;
    }

    public void delete(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }
}
