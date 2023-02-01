package com.nhnacademy.booklay.booklayfront.coupon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import com.nhnacademy.booklay.booklayfront.config.WebConfig;
import com.nhnacademy.booklay.booklayfront.controller.coupon.CouponMemberFrontController;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponDetail;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponHistory;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponIssue;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CouponMemberFrontController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class CouponMemberFrontControllerTest {
    @MockBean
    RestService restService;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    TokenUtils tokenUtils;

    @MockBean
    RedisTemplate<String, Object> redisTemplate;

    private final String RETURN_PAGE = "mypage/myPage";
    private final String URI_PREFIX = "/member/coupon/";

    @Test
    void allCouponList0() throws Exception {
        mockMvc.perform(get(URI_PREFIX + "/list").accept(MediaType.TEXT_HTML))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> result.getResponse().getRedirectedUrl().equals("list/0"));
    }

    //    @Test
    void allCouponList() throws Exception {
        List<Coupon> couponList = new ArrayList<>();
        ResponseEntity<List<Coupon>> responseEntity = new ResponseEntity(couponList, HttpStatus.OK);

        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(object);

        //then
        mockMvc.perform(get(URI_PREFIX + "/list/0").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
            .andExpect(result -> result.getModelAndView().getModel().get("targetUrl")
                .equals("coupon/listView"))
            .andReturn();
    }

    //    @Test
    void couponDetail() throws Exception {
        CouponDetail couponDetail = new CouponDetail(null, "c1", "정율", 1000
            , 101L, "booklay", 10000, 1000,
            LocalDateTime.now(), false, false, 1L, true);
        ResponseEntity<CouponDetail> responseEntity =
            new ResponseEntity(couponDetail, HttpStatus.OK);
        //mocking
        ApiEntity<CouponDetail> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
            ArgumentMatchers.<Class<CouponDetail>>any())).thenReturn(object);


        //then
        mockMvc.perform(get(URI_PREFIX + "/detail/0").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
            .andExpect(result -> result.getModelAndView().getModel().get("targetUrl")
                .equals("coupon/detailView"))
            .andReturn();
    }

    //    @Test
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
        ResponseEntity<List<CouponHistory>> responseEntity =
            new ResponseEntity(historyList, HttpStatus.OK);

        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(object);

        //then
        mockMvc.perform(get(URI_PREFIX + "/history/0").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
            .andExpect(result -> result.getModelAndView().getModel().get("targetUrl")
                .equals("coupon/historyView"))
            .andReturn();
    }

    //    @Test
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
        ResponseEntity<List<CouponIssue>> responseEntity =
            new ResponseEntity(issueList, HttpStatus.OK);

        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(), (ParameterizedTypeReference<Object>) any()))
            .thenReturn(object);


        //then
        mockMvc.perform(get(URI_PREFIX + "/issue/0").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
            .andExpect(result -> result.getModelAndView().getModel().get("targetUrl")
                .equals("coupon/issueView"))
            .andReturn();
    }
}