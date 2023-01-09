package com.nhnacademy.booklay.booklayfront.coupon.service;


import com.nhnacademy.booklay.booklayfront.coupon.domain.ApiEntity;
import java.util.Collections;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestService {


    public <T> ApiEntity<T> post(String url, Map<String, Object> requestBody, Class<T> responseType){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//        headers.addAll(header);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ApiEntity<T> apiEntity = new ApiEntity<>();

        try {
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType);
            apiEntity.setSuccessResponse(response);
        } catch (HttpClientErrorException e){
            apiEntity.setHttpClientErrorException(e);

        }

        return apiEntity;
    }
    public <T> ApiEntity<T> put(String url, Map<String, Object> requestBody, Class<T> responseType){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        ApiEntity<T> apiEntity = new ApiEntity<>();

        try {
            ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PUT, entity, responseType);
            apiEntity.setSuccessResponse(response);
        } catch (HttpClientErrorException e){
            apiEntity.setHttpClientErrorException(e);

        }

        return apiEntity;
    }

    public <T> ApiEntity<T> get(String url, MultiValueMap<String, String> params, ParameterizedTypeReference<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ApiEntity<T> apiEntity = new ApiEntity<>();

        try {
            ResponseEntity<T> response = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, entity, responseType );
            apiEntity.setSuccessResponse(response);
        } catch (HttpClientErrorException e) {
            apiEntity.setHttpClientErrorException(e);
        }

        return apiEntity;
    }

    public <T> ApiEntity<T> get(String url, MultiValueMap<String, String> params, Class<T> responseType) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url).queryParams(params);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ApiEntity<T> apiEntity = new ApiEntity<>();

        try {
            ResponseEntity<T> response = restTemplate.exchange(builder.build().toUriString(), HttpMethod.GET, entity, responseType );
            apiEntity.setSuccessResponse(response);
        } catch (HttpClientErrorException e) {
            apiEntity.setHttpClientErrorException(e);
        }

        return apiEntity;
    }

    public void delete(String url) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(url);
    }
}
