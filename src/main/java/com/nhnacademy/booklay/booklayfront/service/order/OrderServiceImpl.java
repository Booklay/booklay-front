package com.nhnacademy.booklay.booklayfront.service.order;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ORDER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.order.request.TossPaymentConfirmDto;
import com.nhnacademy.booklay.booklayfront.dto.order.response.OrderSheetSaveResponse;
import com.nhnacademy.booklay.booklayfront.exception.SaveOrderException;
import com.nhnacademy.booklay.booklayfront.exception.TossPaymentException;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final ObjectMapper objectMapper;
    private final RestService restService;
    private final String gatewayIp;
    @Override
    public void tossPayment(TossPaymentConfirmDto tossPaymentConfirmDto)
        throws InterruptedException {
        if(tossPaymentConfirmDto.isPaymentAmountOverZero()){
            try {
                HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                    .header("Authorization", "Basic dGVzdF9za19MZXg2QkpHUU9WRGpqcUVHUmVuOFc0dzJ6TmJnOg==")
                    .header("Content-Type", "application/json")
                    .method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(tossPaymentConfirmDto)))
                    .build();
                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                throw new TossPaymentException();
            }
        }
    }

    @Override
    public OrderSheetSaveResponse saveOrder(TossPaymentConfirmDto tossPaymentConfirmDto) {

        String orderSheetUrl = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "payment/", tossPaymentConfirmDto.getOrderId());
        ApiEntity<OrderSheetSaveResponse>
            orderNoApiEntity = restService.get(orderSheetUrl, null, OrderSheetSaveResponse.class);
        if (Boolean.TRUE.equals(orderNoApiEntity.getBody().getIsError())){
            throw new SaveOrderException(orderNoApiEntity.getBody().getErrorMessage());
        }
        return orderNoApiEntity.getBody();
    }
}
