package com.nhnacademy.booklay.booklayfront.service.cart;


import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.CART_REST_PREFIX;
import static com.nhnacademy.booklay.booklayfront.dto.coupon.ControllerStrings.DOMAIN_PREFIX_SHOP;
import static com.nhnacademy.booklay.booklayfront.utils.ControllerUtil.buildString;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final RestService restService;
    private final String gatewayIp;
    @Override
    public void deletePurchasedProductList(List<CartDto> cartDtoList, String cartId) {
        MultiValueMap<String, String> cartDeleteMap = new LinkedMultiValueMap<>();
        cartDeleteMap.add("cartId", cartId);
        cartDeleteMap.put("productNoList", cartDtoList.stream()
            .map(cartDto -> cartDto.getProductNo().toString())
            .collect(Collectors.toList()));
        String cartBuyUrl =
            buildString(gatewayIp, DOMAIN_PREFIX_SHOP, CART_REST_PREFIX, "buy");
        restService.delete(cartBuyUrl, cartDeleteMap);
    }
}
