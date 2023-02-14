package com.nhnacademy.booklay.booklayfront.controller.order;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartObject;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.order.*;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.MemberRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.ProductRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ORDER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getMemberInfoMap;

@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final ProductRestApiModelSettingService productRestApiModelSettingService;
    private final MemberRestApiModelSettingService memberRestApiModelSettingService;
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private static final String STRING_CART_ID = "CART_ID";
    private final String gatewayIp ;
    @ModelAttribute(STRING_CART_ID)
    public String getCookieValue(@CookieValue(name = STRING_CART_ID, required = false)
                                 Optional<Cookie> optionalCookie){
        return optionalCookie.map(Cookie::getValue).orElse(null);
    }
    @PostMapping("/page")
    public String orderPage(@ModelAttribute(STRING_CART_ID)String cartId
            ,@ModelAttribute CartToOrderPageRequest cartToOrderPageRequest, MemberInfo memberInfo
            , Model model){

        List<CartObject> cartObjectList = productRestApiModelSettingService.setProductObjectListToModelByProductNoList(
                cartToOrderPageRequest.getProductNo(), model);
        // 카트에서 넘어온 수량을 적용해줌
        for (CartObject cartObject : cartObjectList) {
            cartObject.setProductCount(
                    cartToOrderPageRequest.getCount().get(
                            cartToOrderPageRequest.getProductNo().indexOf(
                                    cartObject.getProductNo()
                            )
                    )
            );
        }
        model.addAttribute("point", 0);
        if (memberInfo.getMemberId()!=null){
            try {
                memberRestApiModelSettingService.setMemberPointToModelByMemberNo(memberInfo.getMemberNo(), model);
            }catch (Exception e){
                model.addAttribute("point","포인트 조회 실패");
            }
        }

        memberRestApiModelSettingService.setMemberDeliveryDestinationToModelByMemberNo(
                memberInfo.getMemberNo()==null?0L:memberInfo.getMemberNo(), model);

        model.addAttribute("domainIp", "http://localhost:6060");
        model.addAttribute("memberInfo", memberInfo);
        return "order/orderPage";
    }

    @RequestMapping("/success")
    public String saveOrderReceiptAndRedirect(@ModelAttribute TossPaymentConfirmDto tossPaymentConfirmDto){
        //상품 재고 빼기
        String orderSheetUrl = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "sheet/", tossPaymentConfirmDto.getOrderId());
        ApiEntity<OrderSheet> orderSheetApiEntity = restService.get(orderSheetUrl, null, OrderSheet.class);
        if (!orderSheetApiEntity.isSuccess() || orderSheetApiEntity.getBody() == null){
            return "만료된 요청"; //todo
        }

        String storageDownUrl = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "storage/down");
        OrderSheet orderSheet = orderSheetApiEntity.getBody();
        Map<String, Object> storageObjectMap = new HashMap<>();
        storageObjectMap.put("cartDtoList", orderSheet.getCartDtoList());
        ApiEntity<Boolean> storageDownApiEntity = restService.post(storageDownUrl, storageObjectMap, Boolean.class);
        //재고 숫자 감소  todo 미구현
        if (Boolean.TRUE.equals(storageDownApiEntity.getBody())) {
            //결제 승인
            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add("Authorization", "Basic dGVzdF9za19MZXg2QkpHUU9WRGpqcUVHUmVuOFc0dzJ6TmJnOg==");
//            String secretKey = new String(Base64.getEncoder().encode("test_sk_Lex6BJGQOVDjjqEGRen8W4w2zNbg:".getBytes(StandardCharsets.UTF_8)));
//            header.add("Authorization", secretKey);
            Map<String, Object> map = objectMapper.convertValue(tossPaymentConfirmDto, Map.class);
            ApiEntity<TossPaymentResponse> apiEntity = null;
            try{
                apiEntity = restService.post("https://api.tosspayments.com/v1/payments/confirm",header, map, TossPaymentResponse.class);
            }catch (Exception ignored){
            }

//            //승인 실패
//            if (apiEntity==null || !apiEntity.isSuccess()){
//                // 롤백 todo
//                return "결제 실패";
//            }

            //주문 영수증 저장
            String receiptSaveUrl = buildString( gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "receipt/", tossPaymentConfirmDto.getOrderId());
            ApiEntity<Long> orderNoApiEntity = restService.post(receiptSaveUrl, null, Long.class);


            return "redirect:/order/receipt/"+orderNoApiEntity.getBody();
        }else {
            //재고 부족
            return "재고 부족"; //todo
        }

    }

    @GetMapping("fail")
    public String failedOrder(){
        return "결제 실패";
    }


    @GetMapping("receipt/{orderNo}")
    public String orderReceiptPage(@PathVariable String orderNo, MemberInfo memberInfo, Model model){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "receipt/", orderNo);
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.setAll(getMemberInfoMap(memberInfo));
        ApiEntity<OrderReceipt> apiEntity = restService.get(url, multiValueMap, OrderReceipt.class);
        model.addAttribute("orderReceipt", apiEntity.getBody());
        return "order/receipt";
    }

}
