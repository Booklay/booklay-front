package com.nhnacademy.booklay.booklayfront.controller.cart;

import com.nhnacademy.booklay.booklayfront.dto.cart.CartObject;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@ActiveProfiles("test")
@ComponentScan("com.nhnacademy.booklay.booklayfront.config")
@ComponentScan("com.nhnacademy.booklay.booklayfront.service")
class CartControllerTest {
    @MockBean
    RestService restService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    String gatewayIp;

    private String RETURN_PAGE = "cart/listForm";
    private String REDIRECT_PAGE = "cart/list";
    private static final String URI_PREFIX = "/cart/";
    private static final String STRING_CART_ID = "CART_ID";

    @Test
    void cartListForm() throws Exception {
        CartObject cartObject = new CartObject(3L, "dummy", 0L, 1);
        List<CartObject> cartObjectList = new ArrayList<>();
        cartObjectList.add(cartObject);
        ResponseEntity<CartObject> responseEntity =
                new ResponseEntity(cartObjectList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
                (ParameterizedTypeReference<Object>) any())).thenReturn(object);
        Cookie cookie = new Cookie(STRING_CART_ID, "ㅁㄴㅇㅁㅇㅁㅇㅁ");
        mockMvc.perform(get(URI_PREFIX + "/list")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andReturn();
    }

    @Test
    void addProductInCart() {
    }

    @Test
    void deleteProductInCart() {
    }

    @Test
    void deleteAllProductInCart() {
    }

    @Test
    void deleteProductsInCartByBuy() {
    }

    @Test
    void loginAndMoveCartRedisToRDB() {
    }
}