package com.nhnacademy.booklay.booklayfront.controller.cart;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartProductNoListRequest;
import com.nhnacademy.booklay.booklayfront.dto.common.MemberInfo;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import com.nhnacademy.booklay.booklayfront.service.restapimodelsetting.ProductRestApiModelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.CART_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.*;

@SuppressWarnings("unchecked")
@Controller
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;
    private final ProductRestApiModelSettingService productRestApiModelSettingService;
    private static final String STRING_CART_ID = "CART_ID";
    private static final String STRING_PRODUCT_NO = "productNo";
    private static final String STRING_PRODUCT_NO_LIST = "productNoList";
    private static final String REDIRECT_CART_LIST = "redirect:/cart/list";
    private static final String STRING_CART_ID_FOR_API = "cartId";

    @ModelAttribute(STRING_CART_ID)
    public String getCookieValue(@CookieValue(name = STRING_CART_ID, required = false)
                                     Optional<Cookie> optionalCookie,
                                 HttpServletResponse response){
        if (optionalCookie.isPresent()){
            return optionalCookie.get().getValue();
        }
        String sessionId = getRandomUUID();
        Cookie cookie = new Cookie(STRING_CART_ID, sessionId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return sessionId;
    }

    @GetMapping("list")
    public String cartListForm(@ModelAttribute(STRING_CART_ID)String cartId,
                               Model model, MemberInfo memberInfo){
        if (memberInfo.getMemberNo()!=null){
            String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX, "move/", cartId);
            restService.get(url, null, new ParameterizedTypeReference<>() {});
        }
        productRestApiModelSettingService.setProductObjectListToModelByCartId(
                cartId, memberInfo, model);
        return "cart/cartForm";
    }

    @PostMapping
    public String addProductInCart(@ModelAttribute(STRING_CART_ID)String cartId,
                                   @Valid @RequestBody CartDto cartDto, MemberInfo memberInfo){
        Map<String, Object> map = objectMapper.convertValue(cartDto, Map.class);
        map.putAll(getMemberInfoMap(memberInfo));
        map.put(STRING_CART_ID_FOR_API, cartId);
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX);
        restService.post(url, map, String.class);
        return REDIRECT_CART_LIST;
    }

    @GetMapping("delete/{productNo}")
    public String deleteProductInCart(@ModelAttribute(STRING_CART_ID)String cartId,
                                      @PathVariable Long productNo, MemberInfo memberInfo){
        MultiValueMap<String, String> map = getMemberInfoMultiValueMap(memberInfo);
        map.add(STRING_CART_ID_FOR_API, cartId);
        map.add(STRING_PRODUCT_NO, productNo.toString());
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX);
        restService.delete(url, map);
        return REDIRECT_CART_LIST;
    }

    @GetMapping("delete")
    public String deleteAllProductInCart(@ModelAttribute(STRING_CART_ID)String cartId,
                                         MemberInfo memberInfo){
        MultiValueMap<String, String> map = getMemberInfoMultiValueMap(memberInfo);
        map.add(STRING_CART_ID_FOR_API, cartId);
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX, "all");
        restService.delete(url, map);
        return REDIRECT_CART_LIST;
    }

    @PostMapping("buy")
    public String deleteProductsInCartByBuy(@ModelAttribute(STRING_CART_ID)String cartId,
                                            @RequestBody CartProductNoListRequest cartProductNoListRequest,
                                            MemberInfo memberInfo){
        MultiValueMap<String, String> map = getMemberInfoMultiValueMap(memberInfo);
        map.add(STRING_CART_ID_FOR_API, cartId);
        map.put(STRING_PRODUCT_NO_LIST, cartProductNoListRequest.getProductNoList().stream().map(Object::toString).collect(Collectors.toList()));
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX, "buy");
        restService.delete(url, map);
        return REDIRECT_CART_LIST;
    }

    @GetMapping("login")
    public String loginAndMoveCartRedisToRDB(@ModelAttribute(STRING_CART_ID)String cartId, MemberInfo memberInfo){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX, "login/", cartId);
        restService.get(url, getMemberInfoMultiValueMap(memberInfo), String.class);
        return REDIRECT_CART_LIST;
    }

    private String getRandomUUID(){
        String url = buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX, "uuid/");
        while (true){
            String sessionId = UUID.randomUUID().toString();
            ApiEntity<Integer> apiEntity = restService.get(url+sessionId, null, Integer.class);
            if (apiEntity.getBody() == null || apiEntity.getBody() == 1){
                return sessionId;
            }
        }
    }
}
