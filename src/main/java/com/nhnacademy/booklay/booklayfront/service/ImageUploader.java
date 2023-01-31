package com.nhnacademy.booklay.booklayfront.service;

import com.nhnacademy.booklay.booklayfront.dto.storage.response.ObjectFileResponse;
import lombok.RequiredArgsConstructor;
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

import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Objects;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;

@Service
@RequiredArgsConstructor
public class ImageUploader {
    private final RestTemplate restTemplate;
    private final String gatewayIp;

    public void uploadImage(MultipartFile image, Map<String, Object> map) throws IOException {
        if (!Objects.isNull(image) && !image.isEmpty()) {
            URI storageUrl = URI.create(gatewayIp + DOMAIN_PREFIX_SHOP + "/storage");
            ByteArrayResource contentsAsResource = new ByteArrayResource(image.getBytes()) {
                @Override
                public String getFilename() {
                    return image.getOriginalFilename();
                }
            };
            MultipartBodyBuilder resource = new MultipartBodyBuilder();
            resource.part("file",
                    contentsAsResource,
                    MediaType.valueOf(Objects.requireNonNull(image.getContentType())));
            MultiValueMap<String, HttpEntity<?>> multipartBody = resource.build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<MultiValueMap<String, HttpEntity<?>>> httpEntity = new HttpEntity<>(multipartBody,
                    headers);
            ResponseEntity<ObjectFileResponse> responseEntity = restTemplate.postForEntity(storageUrl, httpEntity,
                    ObjectFileResponse.class);
            map.put("imageId", Objects.requireNonNull(responseEntity.getBody()).getId());
        }
    }
}
