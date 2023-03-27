package com.nhnacademy.booklay.booklayfront.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.order.response.OrderCheckResponse;
import com.nhnacademy.booklay.booklayfront.dto.order.OrderSheet;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ORDER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

@SuppressWarnings("unchecked")
@RestController
@RequiredArgsConstructor
@RequestMapping("rest/order")
public class OrderRestController {

    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;
    @PostMapping("check")
    public ResponseEntity<OrderCheckResponse> orderValidCheck(@RequestBody OrderSheet orderSheet, MemberInfo memberInfo) {

        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "check");
        Map<String, Object> map = objectMapper.convertValue(orderSheet, Map.class);
        map.putAll(getMemberInfoMap(memberInfo));

        // 4~500번대가 돌아올경우 서버간의 문제, 200은 처리를 하여 유효하지 않음, 201은 유효한정보라 redis에 임시 저장을 함
        ApiEntity<OrderCheckResponse> apiEntity = restService.post(url, map, OrderCheckResponse.class);
        
        if (apiEntity.getBody() == null || Integer.valueOf(5).equals(apiEntity.getBody().getReasonType())){
            return ResponseEntity.internalServerError().build();
        }else if (Boolean.TRUE.equals(apiEntity.getBody().getValid())){
            return ResponseEntity.ok(apiEntity.getBody());
        } else {
            return ResponseEntity.badRequest().body(apiEntity.getBody());
        }
    }

    @GetMapping("confirm/{orderNo}")
    public ResponseEntity<Boolean> orderConfirm(@PathVariable String orderNo, MemberInfo memberInfo){

        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "confirm/", orderNo);
        ApiEntity<Boolean> apiEntity = restService.get(url, getMemberInfoMultiValueMap(memberInfo), Boolean.class);
        if (Boolean.TRUE.equals(apiEntity.getBody())){
            return ResponseEntity.accepted().body(Boolean.TRUE);
        }else {
            return ResponseEntity.badRequest().body(Boolean.FALSE);
        }
    }

}
