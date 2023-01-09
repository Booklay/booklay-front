package com.nhnacademy.booklay.booklayfront.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class
ApiEntity<T> {
    private ResponseEntity<T> successResponse;
    private HttpClientErrorException httpClientErrorException;

    public T getBody() {
        return successResponse.getBody();
    }

    public boolean isSuccess() {
        return successResponse != null &&
            (
                successResponse.getStatusCode() == HttpStatus.OK
                || successResponse.getStatusCode() == HttpStatus.CREATED
            );
    }

//    public ResponseEntity<Map<String, Object>> getErrorResponse() {
//        try {
//            Map<String, Object> response = JsonUtils.getInstance().fromJson(httpClientErrorException.getResponseBodyAsString(), Map.class);
//            return ResponseEntity.status(httpClientErrorException.getStatusCode()).body(response);
//        } catch (JsonSyntaxException e) {
//            Map<String, Object> response = new HashMap<>();
//            response.put("error", httpClientErrorException.getResponseBodyAsString());
//            return ResponseEntity.status(httpClientErrorException.getStatusCode()).body(response);
//        }
//    }

}
