package com.nhnacademy.booklay.booklayfront.controller.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import com.nhnacademy.booklay.booklayfront.config.WebConfig;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartDto;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartObject;
import com.nhnacademy.booklay.booklayfront.dto.cart.CartProductNoListRequest;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.coupon.PageResponse;
import com.nhnacademy.booklay.booklayfront.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
@ActiveProfiles("test")
@ComponentScan("com.nhnacademy.booklay.booklayfront.service")
@AutoConfigureMockMvc(addFilters = false)
@Import(WebConfig.class)
class CartControllerTest {
    @MockBean
    RestService restService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    String gatewayIp;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    RestTemplate restTemplate;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    TokenUtils tokenUtils;

    private final String RETURN_PAGE = "cart/listForm";
    private final String REDIRECT_PAGE = "cart/list";
    private final String URI_PREFIX = "/cart/";
    private final String STRING_CART_ID = "CART_ID";
    private final String REDIRECT_CART_LIST = "redirect:/cart/list";
    private final String STRING_CART_ID_FOR_API = "cartId";
    private final String STRING_PRODUCT_NO = "productNo";
    private final String STRING_PRODUCT_NO_LIST = "productNoList";

    CartObject cartObject;
    List<CartObject> cartObjectList;
    CartDto cartDto;

    Cookie cookie;

    @BeforeEach
    void before(){
        cartObject = new CartObject(3L, "dummy", 0L, 1);
        cartObjectList = new ArrayList<>();
        cartObjectList.add(cartObject);
        cartDto = new CartDto(cartObject.getProductNo(), cartObject.getProductCount());
        cookie = new Cookie(STRING_CART_ID, "ㅁㄴㅇㅁㅇㅁㅇㅁ");
    }
    @Test
    void cartListForm() throws Exception {
        ResponseEntity<CartObject> responseEntity =
                new ResponseEntity(cartObjectList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
                (ParameterizedTypeReference<Object>) any())).thenReturn(object);
        mockMvc.perform(get(URI_PREFIX + "/list")
                        .cookie(cookie))
                .andExpect(status().isOk())
                .andExpect(result -> Objects.equals(
                    Objects.requireNonNull(result.getModelAndView()).getViewName(), RETURN_PAGE))
                .andReturn();
    }

    @Test
    void addProductInCart() throws Exception {
        mockMvc.perform(post(URI_PREFIX)
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cartDto)))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> Objects.equals(
                Objects.requireNonNull(result.getModelAndView()).getViewName(), REDIRECT_CART_LIST))
            .andReturn();
    }

    @Test
    void deleteProductInCart() throws Exception {
        mockMvc.perform(get(URI_PREFIX + "/delete/"+cartObject.getProductNo())
                .cookie(cookie)
                .param(STRING_CART_ID_FOR_API, cookie.getValue())
                .param(STRING_PRODUCT_NO, cartObject.getProductNo().toString()))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> Objects.equals(
                Objects.requireNonNull(result.getModelAndView()).getViewName(), REDIRECT_CART_LIST))
            .andReturn();
    }

    @Test
    void deleteAllProductInCart() throws Exception {
        mockMvc.perform(get(URI_PREFIX + "/delete/")
                .cookie(cookie)
                .param(STRING_CART_ID_FOR_API, cookie.getValue()))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> Objects.equals(
                Objects.requireNonNull(result.getModelAndView()).getViewName(), REDIRECT_CART_LIST))
            .andReturn();
    }

    @Test
    void deleteProductsInCartByBuy() throws Exception {
        CartProductNoListRequest cartProductNoListRequest = new CartProductNoListRequest(List.of(cartObject.getProductNo()));
        mockMvc.perform(post(URI_PREFIX + "buy")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(cookie)
                .content(objectMapper.writeValueAsString(cartProductNoListRequest)))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> Objects.equals(
                Objects.requireNonNull(result.getModelAndView()).getViewName(), REDIRECT_CART_LIST))
            .andDo(print())
            .andReturn();
    }

    @Test
    void loginAndMoveCartRedisToRDB() throws Exception {
        mockMvc.perform(get(URI_PREFIX + "/login")
                .cookie(cookie))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> Objects.equals(
                Objects.requireNonNull(result.getModelAndView()).getViewName(), REDIRECT_CART_LIST))
            .andReturn();
    }
}