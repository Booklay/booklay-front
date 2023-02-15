package com.nhnacademy.booklay.booklayfront.service;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.storage.response.ObjectFileResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ObjectFileService {

    private final RestTemplate restTemplate;
    private final RestService restService;
    private final String gatewayIp;


    public Long uploadImage(MultipartFile image) throws IOException {
        URI storageUrl = URI.create(gatewayIp + DOMAIN_PREFIX_SHOP + "/storage");

        ByteArrayResource contentsAsResource = new ByteArrayResource(image.getBytes()) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        };

        MultipartBodyBuilder resource = new MultipartBodyBuilder();
        resource.part("file", contentsAsResource,
            MediaType.valueOf(Objects.requireNonNull(image.getContentType())));

        MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity =
            new HttpEntity<>(multipartBody,
                headers);

        ResponseEntity<ObjectFileResponse>
            responseEntity = restTemplate.postForEntity(storageUrl, httpEntity,
            ObjectFileResponse.class);

        return Objects.requireNonNull(responseEntity.getBody()).getId();
    }

    @Cacheable(value = "fileCacheStore", key = "#objectFileId")
    public String loadImage(Long objectFileId) {


        URI uri = URI.create(gatewayIp + DOMAIN_PREFIX_SHOP + "/storage/" + objectFileId);

        ApiEntity<String> response = restService.get(uri.toString(), String.class);

        if (response.isSuccess()) {
            return response.getBody();
        }

        return null;
    }
}
