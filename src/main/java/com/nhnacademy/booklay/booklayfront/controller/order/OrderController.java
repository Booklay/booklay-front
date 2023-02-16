package com.nhnacademy.booklay.booklayfront.controller.order;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartObject;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.common.PageData;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.order.*;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.MemberRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.ProductRestApiModelSettingService;
import io.micrometer.core.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.*;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getDefaultPageMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getMemberInfoMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getMemberInfoMultiValueMap;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.setCurrentPageAndMaxPageToModel;

@Slf4j
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
    private static final String ATTRIBUTE_NAME_ERROR_MESSAGE = "errorMessage";
    private static final String RETURN_PAGE_ORDER_ERROR = "order/orderError";
    @Qualifier("gatewayIp")
    private final String gatewayIp ;
    @Qualifier("domainIp")
    private final String domainIp ;
    @ModelAttribute(STRING_CART_ID)
    public String getCookieValue(@CookieValue(name = STRING_CART_ID, required = false)
                                 Optional<Cookie> optionalCookie){
        return optionalCookie.map(Cookie::getValue).orElse(null);
    }
    @GetMapping("/page")
    public String orderPageGetMapping(){
        return "redirect:/cart/list";
    }
    @PostMapping("/page")
    public String orderPage(@ModelAttribute(STRING_CART_ID)String cartId
            , @Nullable @ModelAttribute CartToOrderPageRequest cartToOrderPageRequest, MemberInfo memberInfo
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

        if (memberInfo.getMemberNo() != null){
            memberRestApiModelSettingService.setMemberDeliveryDestinationToModelByMemberNo(
                memberInfo.getMemberNo(), model);
        }

        model.addAttribute("domainIp", domainIp);
        model.addAttribute("memberInfo", memberInfo);
        return "order/orderPage";
    }

    @RequestMapping("/success")
    public String saveOrderReceiptAndRedirect(@ModelAttribute TossPaymentConfirmDto tossPaymentConfirmDto
            , Model model, MemberInfo memberInfo, @CookieValue(name = STRING_CART_ID, required = false) String cookie){
        String errorReason = null;
        //상품 주문서 받아오기
        String orderSheetUrl = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "sheet/", tossPaymentConfirmDto.getOrderId());
        ApiEntity<OrderSheet> orderSheetApiEntity = restService.get(orderSheetUrl, null, OrderSheet.class);
        if (!orderSheetApiEntity.isSuccess() || orderSheetApiEntity.getBody() == null){
            model.addAttribute(ATTRIBUTE_NAME_ERROR_MESSAGE, "만료된 요청입니다.");
            return RETURN_PAGE_ORDER_ERROR;
        }

        String storageDownUrl = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "storage/down");
        OrderSheet orderSheet = orderSheetApiEntity.getBody();
        Map<String, Object> storageObjectMap = new HashMap<>();
        storageObjectMap.put("cartDtoList", orderSheet.getCartDtoList());
        ApiEntity<Boolean> storageDownApiEntity = restService.post(storageDownUrl, storageObjectMap, Boolean.class);
        //재고 숫자 감소  todo 미구현

        if (Boolean.TRUE.equals(storageDownApiEntity.getBody())) {
        //결제 승인
            try {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.tosspayments.com/v1/payments/confirm"))
                        .header("Authorization", "Basic dGVzdF9za19MZXg2QkpHUU9WRGpqcUVHUmVuOFc0dzJ6TmJnOg==")
                        .header("Content-Type", "application/json")
                        .method("POST", HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(tossPaymentConfirmDto)))
                        .build();
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                log.info("toss response => {}",response.body());
            }catch (IOException | InterruptedException e){
                //todo 감소한 재고 복구
                model.addAttribute(ATTRIBUTE_NAME_ERROR_MESSAGE, "결제 인증에 실패했습니다.");
                return RETURN_PAGE_ORDER_ERROR;
            }
            try {
                //주문 영수증 저장
                errorReason = "주문영수증 저장에 실패하였습니다.";
                String receiptSaveUrl =
                    buildString(gatewayIp, DOMAIN_PREFIX_SHOP, ORDER_REST_PREFIX, "receipt/",
                        tossPaymentConfirmDto.getOrderId());
                ApiEntity<Long> orderNoApiEntity =
                    restService.post(receiptSaveUrl, null, Long.class);


                //장바구니에서 물건 삭제
                errorReason = "장바구니에서 구매한 물건의 삭제에 실패하였습니다.";
                MultiValueMap<String, String> cartDeleteMap =
                    getMemberInfoMultiValueMap(memberInfo);
                cartDeleteMap.add("cartId", cookie);
                cartDeleteMap.put("productNoList", orderSheet.getCartDtoList().stream()
                    .map(cartDto -> cartDto.getProductNo().toString())
                    .collect(Collectors.toList()));
                String cartBuyUrl =
                    buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX, "buy");
                restService.delete(cartBuyUrl, cartDeleteMap);
                return "redirect:/order/receipt/"+orderNoApiEntity.getBody();
            }catch (Exception e){
                model.addAttribute(ATTRIBUTE_NAME_ERROR_MESSAGE, errorReason);
                return RETURN_PAGE_ORDER_ERROR;
            }
        }else {
            //재고 부족
             model.addAttribute(ATTRIBUTE_NAME_ERROR_MESSAGE, "재고가 부족하여 결제에 실패했습니다.");
             return RETURN_PAGE_ORDER_ERROR;
        }

    }

    @GetMapping("fail")
    public String failedOrder(Model model){
        model.addAttribute(ATTRIBUTE_NAME_ERROR_MESSAGE, "토스 페이먼츠 결제 요청에 실패했습니다.");
        return RETURN_PAGE_ORDER_ERROR;
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
