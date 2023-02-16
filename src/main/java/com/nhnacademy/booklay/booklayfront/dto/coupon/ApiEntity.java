package com.nhnacademy.booklay.booklayfront.dto.coupon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
                successResponse.getStatusCode().is2xxSuccessful()
            );
    }

}
