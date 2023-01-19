package com.nhnacademy.booklay.booklayfront.service;


import com.nhnacademy.booklay.booklayfront.dto.domain.ApiEntity;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class RestService {

    private final RestTemplate restTemplate;

    public <T> ApiEntity<T> post(String url, Map<String, Object> requestBody,
                                 Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ApiEntity<T> apiEntity = new ApiEntity<>();

        try {
            ResponseEntity<T> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            apiEntity.setSuccessResponse(response);
        } catch (HttpClientErrorException e) {
            apiEntity.setHttpClientErrorException(e);

        }

        return apiEntity;
    }

    public <T> ApiEntity<T> put(String url, Map<String, Object> requestBody,
                                Class<T> responseType) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ApiEntity<T> apiEntity = new ApiEntity<>();

        try {
            ResponseEntity<T> response =
                restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
            apiEntity.setSuccessResponse(response);
        } catch (HttpClientErrorException e) {
            apiEntity.setHttpClientErrorException(e);

        }

        return apiEntity;
    }

    public <T> ApiEntity<T> get(String url, MultiValueMap<String, String> params,
                                ParameterizedTypeReference<T> responseType) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ApiEntity<T> apiEntity = new ApiEntity<>();

        try {
            ResponseEntity<T> response =
                restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, entity,
                    responseType);
            apiEntity.setSuccessResponse(response);
        } catch (HttpClientErrorException e) {
            apiEntity.setHttpClientErrorException(e);
        }

        return apiEntity;
    }

    public <T> ApiEntity<T> get(String url, MultiValueMap<String, String> params,
                                Class<T> responseType) {

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ApiEntity<T> apiEntity = new ApiEntity<>();

        try {
            ResponseEntity<T> response =
                restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, entity,
                    responseType);
            apiEntity.setSuccessResponse(response);
        } catch (HttpClientErrorException e) {
            apiEntity.setHttpClientErrorException(e);
        }

        return apiEntity;
    }

    public void delete(String url) {
        restTemplate.delete(url);
    }
}
