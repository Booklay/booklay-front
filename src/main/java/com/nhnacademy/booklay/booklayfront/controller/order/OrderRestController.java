package com.nhnacademy.booklay.booklayfront.controller.order;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ORDER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.order.OrderCheckRequest;
import com.nhnacademy.booklay.booklayfront.dto.order.OrderCheckResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("unchecked")
@RestController
@RequiredArgsConstructor
@RequestMapping("rest/order")
public class OrderRestController {

    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;
    @PostMapping("check")
    public ResponseEntity<OrderCheckResponse> orderValidCheck(@RequestBody OrderCheckRequest orderCheckRequest) {

        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "check");
        ApiEntity<OrderCheckResponse> apiEntity = restService.post(url, objectMapper.convertValue(orderCheckRequest, Map.class), OrderCheckResponse.class);
        return ResponseEntity.ok(apiEntity.getBody());
    }

}
