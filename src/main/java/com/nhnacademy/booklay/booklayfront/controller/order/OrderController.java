package com.nhnacademy.booklay.booklayfront.controller.order;


import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.ORDER_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.getMemberInfoMap;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartObject;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.order.OrderReceipt;
import com.nhnacademy.booklay.booklayfront.dto.order.request.CartToOrderPageRequest;
import com.nhnacademy.booklay.booklayfront.dto.order.request.TossPaymentConfirmDto;
import com.nhnacademy.booklay.booklayfront.dto.order.response.OrderSheetSaveResponse;
import com.nhnacademy.booklay.booklayfront.exception.TossPaymentException;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.cart.CartService;
import com.nhnacademy.booklay.booklayfront.service.order.OrderService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.MemberRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.ProductRestApiModelSettingService;
import io.micrometer.core.lang.Nullable;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final ProductRestApiModelSettingService productRestApiModelSettingService;
    private final MemberRestApiModelSettingService memberRestApiModelSettingService;
    private final OrderService orderService;
    private final CartService cartService;
    private final RestService restService;
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
    public String orderPage(@ModelAttribute(STRING_CART_ID)String cartId
            , @Nullable @ModelAttribute CartToOrderPageRequest cartToOrderPageRequest, MemberInfo memberInfo
            , Model model){

        if (cartToOrderPageRequest == null || cartToOrderPageRequest.getProductNo() == null || cartToOrderPageRequest.getProductNo().isEmpty()){
            return "redirect:/cart/list";
        }
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
    public String saveOrderReceiptAndRedirect(@ModelAttribute
                                              TossPaymentConfirmDto tossPaymentConfirmDto
            , @CookieValue(name = STRING_CART_ID, required = false) String cartId)
        throws InterruptedException {
        //상품 주문서 받아오기
        OrderSheetSaveResponse orderSheetSaveResponse = orderService.saveOrder(tossPaymentConfirmDto);
        orderService.tossPayment(tossPaymentConfirmDto);
        cartService.deletePurchasedProductList( orderSheetSaveResponse.getCartDtoList(), cartId);

        return "redirect:/order/receipt/"+orderSheetSaveResponse.getOrderId();
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


    @ExceptionHandler(TossPaymentException.class)
    public String tossPaymentException(Model model){
        model.addAttribute(ATTRIBUTE_NAME_ERROR_MESSAGE, "토스 페이먼츠 결제 중 오류가 발생했습니다.");
        return RETURN_PAGE_ORDER_ERROR;
    }

}
