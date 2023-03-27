package com.nhnacademy.booklay.booklayfront.service.cart;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import java.util.List;

public interface CartService {
    void deletePurchasedProductList(List<CartDto> cartDtoList, String cartId);
}
