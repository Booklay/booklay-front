package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.nhnacademy.booklay.booklayfront.coupon.domain.*;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import org.junit.jupiter.api.Test;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponMemberFrontController.class)
class CouponMemberFrontControllerTest {
    @MockBean
    RestService restService;
    @Autowired
    MockMvc mockMvc;
    private final String RETURN_PAGE = "member/memberPage";
    private final String URI_PREFIX = "/member/coupon";

    @Test
    void allCouponList0() throws Exception {
        mockMvc.perform(get(URI_PREFIX+"/list").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals("list/0"));
    }

    @Test
    void allCouponList() throws Exception {
        List<Coupon> couponList = new ArrayList<>();
        ApiEntity<List<Coupon>> apiEntity = new ApiEntity<>();
        ResponseEntity<List<Coupon>> responseEntity = new ResponseEntity(couponList, HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        //mocking
        String url = FrontURI.SHOP_URI + "member/coupon/list/" + 0;

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("memberId", anyString());
        when(restService.get(url, map, new ParameterizedTypeReference<List<Coupon>>(){}))
                .thenReturn(apiEntity);

        //then
        mockMvc.perform(get(URI_PREFIX+"/list/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/listView"))
                .andReturn();
    }

    @Test
    void couponDetail() throws Exception {
        CouponDetail couponDetail = new CouponDetail(null, "c1", 0L, "정액", 1000L
                , 101L, 123L, 10000L, 1000L,
                LocalDate.now(),false, "");

        ApiEntity<CouponDetail> apiEntity = new ApiEntity<>();
        ResponseEntity<CouponDetail> responseEntity = new ResponseEntity(couponDetail, HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        //mocking
        String url = FrontURI.SHOP_URI + "member/coupon/detail/" + 0;
        when(restService.get(url, null, CouponDetail.class))
                .thenReturn(apiEntity);

        //then
        mockMvc.perform(get(URI_PREFIX+"/detail/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/detailView"))
                .andReturn();
    }

    @Test
    void couponHistory() throws Exception {
        CouponHistory couponHistory = CouponHistory.builder()
                .userId(0L)
                .couponId(0L)
                .code("1234-gfdd-1234")
                .orderId(0L)
                .orderedAt(LocalDate.now())
                .name("쿠폰1")
                .build();
        List<CouponHistory> historyList = new ArrayList<>();
        historyList.add(couponHistory);
        ApiEntity<List<CouponHistory>> apiEntity = new ApiEntity<>();
        ResponseEntity<List<CouponHistory>> responseEntity = new ResponseEntity(historyList, HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        String url = FrontURI.SHOP_URI + "member/coupon/history/" + 0;
        //mocking
        when(restService.get(url, null, new ParameterizedTypeReference<List<CouponHistory>>(){}))
                .thenReturn(apiEntity);

        //then
        mockMvc.perform(get(URI_PREFIX+"/history/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/historyView"))
                .andReturn();
    }

    @Test
    void couponIssuing() throws Exception {
        CouponIssue couponIssue = CouponIssue.builder()
                .userId(0L)
                .couponId(0L)
                .code("1234-gfdd-1234")
                .issuedAt(LocalDate.now())
                .name("쿠폰1")
                .build();
        List<CouponIssue> issueList = new ArrayList<>();
        issueList.add(couponIssue);
        ApiEntity<List<CouponIssue>> apiEntity = new ApiEntity<>();
        ResponseEntity<List<CouponIssue>> responseEntity = new ResponseEntity(issueList, HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        String url = FrontURI.SHOP_URI + "member/coupon/issue/" + 0;
        //mocking
        when(restService.get(url, null, new ParameterizedTypeReference<List<CouponIssue>>(){}))
                .thenReturn(apiEntity);

        //then
        mockMvc.perform(get(URI_PREFIX+"/issue/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/issueView"))
                .andReturn();
    }
}