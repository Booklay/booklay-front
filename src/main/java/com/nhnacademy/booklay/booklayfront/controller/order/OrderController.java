package com.nhnacademy.booklay.booklayfront.controller.order;


import com.nhnacademy.booklay.booklayfront.dto.cart.CartObject;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.order.CartToOrderPageRequest;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.MemberRestApiModelSettingService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.ProductRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("order")
public class OrderController {
    private final ProductRestApiModelSettingService productRestApiModelSettingService;
    private final MemberRestApiModelSettingService memberRestApiModelSettingService;
    private static final String STRING_CART_ID = "CART_ID";
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
                model.addAttribute("point", "포인트 조회 실패");
            }
        }

        memberRestApiModelSettingService.setMemberDeliveryDestinationToModelByMemberNo(
                memberInfo.getMemberNo()==null?0L:memberInfo.getMemberNo(), model);

        model.addAttribute("memberInfo", memberInfo);
        return "order/orderPage";
    }

}
