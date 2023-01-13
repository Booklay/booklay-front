package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.nhnacademy.booklay.booklayfront.coupon.domain.*;
import com.nhnacademy.booklay.booklayfront.coupon.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CouponAdminFrontController.class)
@ActiveProfiles("test")
@ComponentScan("com.nhnacademy.booklay.booklayfront.config")
class CouponAdminFrontControllerTest {
    @MockBean
    RestService restService;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ImageUploader imageUploader;
    private String RETURN_PAGE = "admin/adminPage";
    private static final String URI_PREFIX = "/admin/coupon";

    @Autowired
    String gatewayIp;
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
        ResponseEntity<List<Coupon>> responseEntity = new ResponseEntity(couponList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
//        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any())).thenReturn(object);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any())).thenReturn(object);

        mockMvc.perform(get(URI_PREFIX+"/list/0"))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/listView"))
                .andDo(print())
                .andReturn();
    }

    @Test
    void allCouponTypeList() throws Exception {
        List<CouponType> couponList = new ArrayList<>();
        ResponseEntity<List<CouponType>> responseEntity = new ResponseEntity(couponList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any())).thenReturn(object);

        mockMvc.perform(get(URI_PREFIX+"/list/type/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/typeListView"))
                .andReturn();
    }


    @Test
    void memberCouponList() throws Exception {
        List<Coupon> couponList = new ArrayList<>();
        ResponseEntity<List<Coupon>> responseEntity = new ResponseEntity(couponList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any())).thenReturn(object);

        mockMvc.perform(get(URI_PREFIX+"/list/0/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/listView"))
                .andReturn();
    }

    @Test
    void viewCoupon() throws Exception {
        CouponDetail couponDetail = new CouponDetail(null, "c1", 0L, "정액", 1000L
                , 101L, 123L, 10000L, 1000L,
                LocalDate.now(),false, "");
        ResponseEntity<CouponDetail> responseEntity = new ResponseEntity(couponDetail, HttpStatus.OK);
        //mocking
        ApiEntity<CouponDetail> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), ArgumentMatchers.<Class<CouponDetail>>any())).thenReturn(object);

        mockMvc.perform(get(URI_PREFIX+"/detail/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/detailView"))
                .andDo(print())
                .andReturn();
    }

    @Test
    void updateCouponForm() throws Exception {
        CouponDetail couponDetail = new CouponDetail(null, "c1", 0L, "정액", 1000L
                , 101L, 123L, 10000L, 1000L,
                LocalDate.now(),false, "");
        ResponseEntity<CouponDetail> responseEntity = new ResponseEntity(couponDetail, HttpStatus.OK);
        //mocking
        ApiEntity<CouponDetail> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), ArgumentMatchers.<Class<CouponDetail>>any())).thenReturn(object);

        mockMvc.perform(get(URI_PREFIX+"/update/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/couponUpdateForm"))
                .andReturn();
    }

    @Test
    void postUpdateCouponForm() throws Exception {
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
        ResponseEntity<String> responseEntity = new ResponseEntity("couponList", HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        //mocking
        when(restService.put(anyString(), anyMap(), ArgumentMatchers.<Class<String>>any()))
                .thenReturn(apiEntity);

        mockMvc.perform(post(URI_PREFIX+"/update/0").accept(MediaType.TEXT_HTML)
                        .params(map)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals(""));
    }

    @Test
    void deleteCoupon() throws Exception {
        mockMvc.perform(get(URI_PREFIX+"/delete/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().is3xxRedirection())
                .andExpect(result -> result.getResponse().getRedirectedUrl().equals(""));
    }

    @Test
    void historyCoupon() throws Exception {
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
        ResponseEntity<List<CouponHistory>> responseEntity = new ResponseEntity(historyList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any())).thenReturn(object);


        //then
        mockMvc.perform(get(URI_PREFIX+"/history/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/historyView"))
                .andReturn();
    }

    @Test
    void memberHistoryCoupon() throws Exception {
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
        ResponseEntity<List<CouponHistory>> responseEntity = new ResponseEntity(historyList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any())).thenReturn(object);


        //then
        mockMvc.perform(get(URI_PREFIX+"/history/0/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/historyView"))
                .andReturn();
    }

    @Test
    void issueCoupon() throws Exception {
        CouponIssue couponIssue = CouponIssue.builder()
                .userId(0L)
                .couponId(0L)
                .code("1234-gfdd-1234")
                .issuedAt(LocalDate.now())
                .name("쿠폰1")
                .build();
        List<CouponIssue> issueList = new ArrayList<>();
        issueList.add(couponIssue);
        ResponseEntity<List<CouponIssue>> responseEntity = new ResponseEntity(issueList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any())).thenReturn(object);


        //then
        mockMvc.perform(get(URI_PREFIX+"/issue/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/issueView"))
                .andReturn();
    }

    @Test
    void testIssueCoupon() throws Exception {
        CouponIssue couponIssue = CouponIssue.builder()
                .userId(0L)
                .couponId(0L)
                .code("1234-gfdd-1234")
                .issuedAt(LocalDate.now())
                .name("쿠폰1")
                .build();
        List<CouponIssue> issueList = new ArrayList<>();
        issueList.add(couponIssue);
        ResponseEntity<List<CouponIssue>> responseEntity = new ResponseEntity(issueList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any())).thenReturn(object);

        //then
        mockMvc.perform(get(URI_PREFIX+"/issue/0/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andExpect(result -> result.getModelAndView().getModel().get("targetUrl").equals("coupon/issueView"))
                .andReturn();
    }

//    @Test
//    void issuingCouponForm() {
//    }
//
//    @Test
//    void memberIssuingCoupon() {
//    }

}