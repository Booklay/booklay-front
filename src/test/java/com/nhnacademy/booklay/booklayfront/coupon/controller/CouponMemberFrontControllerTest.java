package com.nhnacademy.booklay.booklayfront.coupon.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.booklay.booklayfront.coupon.domain.ApiEntity;
import com.nhnacademy.booklay.booklayfront.coupon.domain.Coupon;
import com.nhnacademy.booklay.booklayfront.coupon.domain.FrontURI;
import com.nhnacademy.booklay.booklayfront.coupon.service.RestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.web.SpringBootMockServletContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponMemberFrontController.class)
//@WebAppConfiguration
class CouponMemberFrontControllerTest {
    @MockBean
    RestService restService;

    @Autowired
    CouponMemberFrontController couponMemberFrontController;

    FrontURI frontURI = new FrontURI();
    @Autowired
    MockMvc mockMvc;
    ObjectMapper objectMapper;
    private String RETURN_PAGE = "member/memberPage";
//    Member member;
//    MemberRetrieveResponse responseDto;
//    MemberCreateRequest createDto;

    private static final String URI_PREFIX = "/member/coupon";

//    @Test
//    @DisplayName("회원의 본인정보 조회 성공 테스트")
//    void testRetrieveMember() throws Exception {
//        //mocking
//        when(memberService.retrieveMember(member.getMemberNo())).thenReturn(responseDto);
//
//        //then
//        mockMvc.perform(get(URI_PREFIX + "/" + member.getMemberNo())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print())
//                .andReturn();
//    }
    @Test
    void memberCouponPage() {

    }

    @Test
    void addNavHead() throws Exception {
        List<Coupon> couponList = new ArrayList<>();
        ApiEntity<List<Coupon>> apiEntity = new ApiEntity<>();
        ReflectionTestUtils.setField(apiEntity, "body", couponList);
        //mocking
        String url = frontURI.SHOPURI + "member/coupon/list/" + 0;
        when(restService.get(url, null, new ParameterizedTypeReference<List<Coupon>>(){}))
                .thenReturn(apiEntity);

        //then

        mockMvc.perform(get(URI_PREFIX+"/list/0").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
                .andDo(print())
                .andReturn();

    }

    @Test
    void allCouponList0() {
    }

    @Test
    void allCouponList() {
    }

    @Test
    void couponDetail() {
    }

    @Test
    void couponHistory() {
    }

    @Test
    void couponIssuing() {
    }
}