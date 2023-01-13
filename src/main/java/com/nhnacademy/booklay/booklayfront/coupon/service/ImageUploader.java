package com.nhnacademy.booklay.booklayfront.coupon.service;

import com.nhnacademy.booklay.booklayfront.coupon.domain.ApiEntity;
import com.nhnacademy.booklay.booklayfront.coupon.domain.ImageUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ImageUploader {
    private final RestService restService;
    public String uploadImage(MultipartFile multipartFile, HttpServletRequest request) {
        if (Objects.isNull(multipartFile)) {
            Map<String, Object> map = new HashMap<>();
            //todo url, 이미지 명칭, 타입, ImageUploadResponse 명칭
            map.put("image", multipartFile);
            ApiEntity<ImageUploadResponse> apiEntity = restService.post("", map, ImageUploadResponse.class);
            if (apiEntity.isSuccess()){
                return apiEntity.getBody().getImagePath();
            }
        }
        return "defaultImage";
    }
}
