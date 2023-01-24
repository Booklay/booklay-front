package com.nhnacademy.booklay.booklayfront.controller.cart;


import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.CART_URL_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.REST_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("cart")
public class CartController {
    private final RestService restService;
    private final ObjectMapper objectMapper;
    private final String gatewayIp;
    private static final String STRING_CART_ID = "CART_ID";
    private static final String STRING_PRODUCT_NO = "productNo";
    private static final String STRING_PRODUCT_NO_LIST = "productNoList";
    private static final String ATTRIBUTE_NAME_CART_DTO_LIST = "cartDtoList";

    @ModelAttribute(STRING_CART_ID)
    public String getCookieValue(@CookieValue(name = STRING_CART_ID, required = false)
                                     Optional<Cookie> optionalCookie,
                                 HttpServletResponse response){
        if (optionalCookie.isPresent()){
            return optionalCookie.get().getValue();
        }
        String sessionId = getRandomUUID();
        Cookie cookie = new Cookie(STRING_CART_ID, sessionId);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return sessionId;
    }

    @GetMapping("list")
    public String cartListForm(@ModelAttribute(STRING_CART_ID)String cartId,
                               Model model){
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add(STRING_CART_ID, cartId);
        String url = buildString(gatewayIp, REST_PREFIX_SHOP, CART_URL_PREFIX);
        ApiEntity<List<CartDto>> apiEntity = restService.get(url, multiValueMap, new ParameterizedTypeReference<>() {});
        List<CartDto> cartDtoList = apiEntity.getBody();
        model.addAttribute(ATTRIBUTE_NAME_CART_DTO_LIST, cartDtoList);
        return "cart/cartListForm";
    }

    @PostMapping
    public String addProductInCart(@ModelAttribute(STRING_CART_ID)String cartId,
                                   @RequestBody CartDto cartDto){
        Map<String, Object> map = objectMapper.convertValue(cartDto, Map.class);
        map.put(STRING_CART_ID, cartId);
        String url = buildString(gatewayIp, REST_PREFIX_SHOP, CART_URL_PREFIX);
        restService.post(url, map, String.class);
        return "redirect:cart/list";
    }

    @GetMapping("delete/{productNo}")
    public String deleteProductInCart(@ModelAttribute(STRING_CART_ID)String cartId,
                                      @PathVariable Long productNo){
        Map<String, Object> map = new HashMap<>();
        map.put(STRING_CART_ID, cartId);
        map.put(STRING_PRODUCT_NO, productNo);
        String url = buildString(gatewayIp, REST_PREFIX_SHOP, CART_URL_PREFIX);
        restService.post(url, map, String.class);
        return "redirect:cart/list";
    }

    @GetMapping("delete")
    public String deleteAllProductInCart(@ModelAttribute(STRING_CART_ID)String cartId){
        Map<String, Object> map = new HashMap<>();
        map.put(STRING_CART_ID, cartId);
        String url = buildString(gatewayIp, REST_PREFIX_SHOP, CART_URL_PREFIX, "all");
        restService.post(url, map, String.class);
        return "redirect:cart/list";
    }

    @PostMapping("buy")
    public String deleteProductsInCartByBuy(@ModelAttribute(STRING_CART_ID)String cartId,
                                            @RequestBody List<Long> productNoList){
        Map<String, Object> map = new HashMap<>();
        map.put(STRING_CART_ID, cartId);
        map.put(STRING_PRODUCT_NO_LIST, productNoList);
        String url = buildString(gatewayIp, REST_PREFIX_SHOP, CART_URL_PREFIX, "buy");
        restService.post(url, map, String.class);
        return "redirect:cart/list";
    }

    private String getRandomUUID(){
        String url = buildString(gatewayIp, REST_PREFIX_SHOP, CART_URL_PREFIX, "uuid/");
        while (true){
            String sessionId = UUID.randomUUID().toString();
            ApiEntity<Integer> apiEntity = restService.get(url+sessionId, null, Integer.class);
            if (apiEntity.getBody() == null || apiEntity.getBody() == 1){
                return sessionId;
            }
        }
    }
}
