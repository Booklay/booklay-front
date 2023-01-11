package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.nhnacademy.booklay.booklayfront.coupon.domain.*;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.ZoneId;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CouponAdminFrontController.class)
class CouponAdminFrontControllerTest {
    @MockBean
    RestService restService;
    @Autowired
    MockMvc mockMvc;
    private String RETURN_PAGE = "admin/adminPage";
    private static final String URI_PREFIX = "/admin/coupon";

    @Test
    void createCouponTypeForm() throws Exception {
        //then
        mockMvc.perform(get(URI_PREFIX+"/create").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/createCouponTypeForm"))
                .andReturn();
    }

    @Test
    void postCreateCouponType() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "coupon1");
        map.add("userId", "1");
        map.add("typeName", "정액");
        map.add("amount", "1000");
        map.add("categoryId", "12");
        map.add("productId", "3");
        map.add("minimumUseAmount", "10000");
        map.add("maximumDiscountAmount", "1000");
        map.add("issuanceDeadline", "2030-10-30T12:34");
        map.add("isDuplicatable", "true");
        map.add("issueAmount", "500");

        ApiEntity<String> apiEntity = new ApiEntity<>();
        ResponseEntity<List<Coupon>> responseEntity = new ResponseEntity("couponList", HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        //mocking
        when(restService.post(anyString(), anyMap(), ArgumentMatchers.<Class<String>>any()))
                .thenReturn(apiEntity);

        mockMvc.perform(post(URI_PREFIX+"/create").accept(MediaType.TEXT_HTML)
                        .params(map)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals(""));
    }

    @Test
    void allCouponList0() throws Exception {
        mockMvc.perform(get(URI_PREFIX+"/list").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("list/0"));
    }

    @Test
    void allCouponList() throws Exception {
        Coupon coupon = Coupon.builder()
                .couponId(0L)
                .typeName("정액")
                .name("coupon1")
                .amount(1000L)
                .maximumDiscountAmount(1000L)
                .minimumUseAmount(10000L)
                .build();
        List<Coupon> couponList = new ArrayList<>();
        couponList.add(coupon);
        ApiEntity<List<Coupon>> apiEntity = new ApiEntity<>();
        ResponseEntity<List<Coupon>> responseEntity = new ResponseEntity(couponList, HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        //mocking
        when(restService.post(anyString(), anyMap(), ArgumentMatchers.<Class<List<Coupon>>>any()))
                .thenReturn(apiEntity);

        mockMvc.perform(get(URI_PREFIX+"/list/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/listView"))
                .andReturn();
    }

    @Test
    void allCouponTypeList() throws Exception {
        List<CouponType> couponList = new ArrayList<>();

        ApiEntity<List<CouponType>> apiEntity = new ApiEntity<>();
        ResponseEntity<List<CouponType>> responseEntity = new ResponseEntity(couponList, HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        //mocking
        //Matchers.<ParameterizedTypeReference<List<CouponType>>>any())
        ParameterizedTypeReference parameterizedTypeReference = new ParameterizedTypeReference<List<CouponType>>(){};
//        when(restService.post(anyString(), anyMap(), ArgumentMatchers.<>any()))
//                .thenReturn(apiEntity);

        mockMvc.perform(get(URI_PREFIX+"/list/type/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("list/0"));
    }


    @Test
    void memberCouponList() {
    }

    @Test
    void viewCoupon() {
    }

    @Test
    void updateCouponForm() {
    }

    @Test
    void postUpdateCouponForm() {
    }

    @Test
    void deleteCoupon() {
    }

    @Test
    void historyCoupon() {
    }

    @Test
    void testHistoryCoupon() {
    }

    @Test
    void issueCoupon() {
    }

    @Test
    void testIssueCoupon() {
    }

    @Test
    void issuingCouponForm() {
    }

    @Test
    void memberIssuingCoupon() {
    }
}