package com.nhnacademy.booklay.booklayfront.controller.order;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartObject;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings;
import com.nhnacademy.booklay.booklayfront.dto.order.CartToOrderPageRequest;
import com.nhnacademy.booklay.booklayfront.dto.order.TossPaymentConfirmDto;
import com.nhnacademy.booklay.booklayfront.dto.order.TossPaymentResponse;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.MemberRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.ProductRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.utils.ControllerUtil;
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
    private static String gatewayIp ;
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

        model.addAttribute("memberInfo", memberInfo);
        return "order/orderPage";
    }

    @GetMapping
    public String saveOrderRecipeAndRedirect(@ModelAttribute TossPaymentConfirmDto tossPaymentConfirmDto){
        //상품 재고 빼기
        if (true) {
            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add("Authorization", "Basic");
            header.add("Authorization", "dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==");
            header.add("Content-Type", "application/json");
            Map<String, Object> map = objectMapper.convertValue(tossPaymentConfirmDto, Map.class);
            ApiEntity<TossPaymentResponse> apiEntity = restService.post("https://api.tosspayments.com/v1/payments/confirm",header, map, TossPaymentResponse.class);

            //주문 영수증 저장
            String recipeSaveUrl = ControllerUtil.buildString( gatewayIp, DOMAIN_PREFIX_SHOP);



//            restService.post()
            return "redirect:";
        }else {
            return "결제 실패";
        }

    }

}
