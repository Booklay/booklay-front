package com.nhnacademy.booklay.booklayfront.coupon.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.booklay.booklayfront.auth.jwt.TokenUtils;
import com.nhnacademy.booklay.booklayfront.config.RedisConfig;
import com.nhnacademy.booklay.booklayfront.config.WebConfig;
import com.nhnacademy.booklay.booklayfront.controller.admin.coupon.CouponAdminFrontController;
import com.nhnacademy.booklay.booklayfront.controller.admin.coupon.CouponHistoryAdminFrontController;
import com.nhnacademy.booklay.booklayfront.controller.admin.coupon.CouponIssueAdminFrontController;
import com.nhnacademy.booklay.booklayfront.controller.admin.coupon.CouponTypeAdminController;
import com.nhnacademy.booklay.booklayfront.dto.PageResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.ApiEntity;
import com.nhnacademy.booklay.booklayfront.dto.coupon.Coupon;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponDetail;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponHistory;
import com.nhnacademy.booklay.booklayfront.dto.coupon.CouponIssue;
import com.nhnacademy.booklay.booklayfront.dto.coupon.response.CouponHistoryResponse;
import com.nhnacademy.booklay.booklayfront.dto.coupon.type.CouponType;
import com.nhnacademy.booklay.booklayfront.service.ImageUploader;
import com.nhnacademy.booklay.booklayfront.service.RestService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;



@WebMvcTest(controllers = {CouponAdminFrontController.class
        , CouponHistoryAdminFrontController.class
        , CouponTypeAdminController.class
        , CouponIssueAdminFrontController.class})
@ActiveProfiles("test")
@ComponentScan("com.nhnacademy.booklay.booklayfront.service")
@AutoConfigureMockMvc(addFilters = false)
@Import({WebConfig.class, RedisConfig.class})
class CouponAdminFrontControllerTest {
    @MockBean
    RestService restService;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    RestTemplate restTemplate;

    @MockBean
    ImageUploader imageUploader;

    @MockBean
    TokenUtils tokenUtils;

    @MockBean
    RedisTemplate<String, Object> redisTemplate;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String RETURN_PAGE = "admin/adminPage";
    private static final String URI_PREFIX = "/admin/coupons";

    @Autowired
    String gatewayIp;
    CouponHistoryResponse couponHistoryResponse;
    CouponHistory couponHistory;

    @BeforeEach
    void before() {
        couponHistoryResponse = new CouponHistoryResponse(
                0L, "1234-gfdd-1234", "쿠폰1", "id"
                , LocalDateTime.now(), LocalDateTime.now());
        couponHistory = CouponHistory.builder()
                .userId(0L)
                .couponId(0L)
                .code("1234-gfdd-1234")
                .orderId(0L)
                .orderedAt(LocalDate.now())
                .name("쿠폰1")
                .build();
    }
    @Test
    void createCouponForm() throws Exception {
        List<CouponType> couponTypes = new ArrayList<>();
        couponTypes.add(new CouponType(1L, "dummyType"));
        PageResponse<CouponType> couponTypePageResponse = new PageResponse<>();
        ReflectionTestUtils.setField(couponTypePageResponse, "pageNumber", 0);
        ReflectionTestUtils.setField(couponTypePageResponse, "pageSize", 20);
        ReflectionTestUtils.setField(couponTypePageResponse, "totalPages", 0);
        ReflectionTestUtils.setField(couponTypePageResponse, "data", couponTypes);
        ResponseEntity<PageResponse<CouponType>> responseEntity =
            new ResponseEntity<>(couponTypePageResponse, HttpStatus.OK);
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        //mocking
        when(restService.get(anyString(), any(),
            (ParameterizedTypeReference<Object>) any())).thenReturn(object);
        //then
        mockMvc.perform(get(URI_PREFIX+"/create-form").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName()
                .equals("coupon/createCouponForm"))
            .andReturn();
    }

    @Test
    void postCreateCoupon() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "coupon1");
        map.add("userId", "1");
        map.add("typeCode", "1");
        map.add("isOrderCoupon", "true");
        map.add("applyItemId", "12");
        map.add("amount", "1000");
        map.add("minimumUseAmount", "10000");
        map.add("maximumDiscountAmount", "1000");
        map.add("issuanceDeadlineAt", "2030-10-30T12:34");
        map.add("isDuplicatable", "true");
        map.add("isLimited", "true");
        map.add("validateTerm", "10");

        ApiEntity<String> apiEntity = new ApiEntity<>();
        ResponseEntity<List<Coupon>> responseEntity =
            new ResponseEntity("couponList", HttpStatus.OK);
        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        //mocking
        when(restService.post(anyString(), anyMap(), ArgumentMatchers.<Class<String>>any()))
            .thenReturn(apiEntity);

        mockMvc.perform(post(URI_PREFIX + "/create-form").accept(MediaType.TEXT_HTML)
                .params(map)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> result.getResponse().getRedirectedUrl().equals(""));
    }

    @Test
    void allCouponList() throws Exception {
        Coupon coupon = new Coupon(0L, "coupon1", "정액"
            , 1000, 1000, 10000, false);
        List<Coupon> couponList = new ArrayList<>();
        PageResponse<Coupon> couponPageResponse = new PageResponse<Coupon>();
        couponList.add(coupon);
        ReflectionTestUtils.setField(couponPageResponse, "pageNumber", 0);
        ReflectionTestUtils.setField(couponPageResponse, "pageSize", 20);
        ReflectionTestUtils.setField(couponPageResponse, "totalPages", 0);
        ReflectionTestUtils.setField(couponPageResponse, "data", couponList);
        ResponseEntity<PageResponse<Coupon>> responseEntity =
            new ResponseEntity<>(couponPageResponse, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
            (ParameterizedTypeReference<Object>) any())).thenReturn(object);

        mockMvc.perform(get(URI_PREFIX + "/list")
                        .param("page", "0"))
            .andExpect(status().isOk())
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName().equals("coupon/listView"))
            .andDo(print())
            .andReturn();
    }

    @Test
    void allCouponTypeList() throws Exception {
        List<CouponType> couponTypes = new ArrayList<>();
        PageResponse<CouponType> couponPageResponse = new PageResponse<CouponType>();
        ReflectionTestUtils.setField(couponPageResponse, "pageNumber", 0);
        ReflectionTestUtils.setField(couponPageResponse, "pageSize", 20);
        ReflectionTestUtils.setField(couponPageResponse, "totalPages", 0);
        ReflectionTestUtils.setField(couponPageResponse, "data", couponTypes);
        ResponseEntity<PageResponse<CouponType>> responseEntity =
            new ResponseEntity<>(couponPageResponse, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
            (ParameterizedTypeReference<Object>) any())).thenReturn(object);

        mockMvc.perform(get(URI_PREFIX + "/types/list").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName()
                .equals("coupon/type/typeListView"))
            .andReturn();
    }


    @Test
    void memberCouponList() throws Exception {
        List<Coupon> couponList = new ArrayList<>();

        PageResponse<Coupon> couponPageResponse = new PageResponse<>();
        ReflectionTestUtils.setField(couponPageResponse, "pageNumber", 0);
        ReflectionTestUtils.setField(couponPageResponse, "pageSize", 20);
        ReflectionTestUtils.setField(couponPageResponse, "totalPages", 0);
        ReflectionTestUtils.setField(couponPageResponse, "data", couponList);
        ResponseEntity<PageResponse<Coupon>> responseEntity = new ResponseEntity<>(couponPageResponse, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
            (ParameterizedTypeReference<Object>) any())).thenReturn(object);
        mockMvc.perform(get(URI_PREFIX + "/list/0").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName().equals("coupon/listView"))
            .andReturn();
    }

    @Test
    void viewCoupon() throws Exception {
        CouponDetail couponDetail = new CouponDetail(null, "c1", "정률", 1000
            , 101L, "booklay", 10000, 1000, false, false, 1L, true);
        ResponseEntity<CouponDetail> responseEntity =
            new ResponseEntity<>(couponDetail, HttpStatus.OK);
        //mocking
        ApiEntity<CouponDetail> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
            ArgumentMatchers.<Class<CouponDetail>>any())).thenReturn(object);

        mockMvc.perform(get(URI_PREFIX + "/detail/0").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName().equals("coupon/detailView"))
            .andDo(print())
            .andReturn();
    }

//    @Test
//    void updateCouponForm() throws Exception {
//        CouponDetail couponDetail = new CouponDetail(null, "c1", 0L, 0L, 1000L
//            , 101L, 123L, 10000L, 1000L,
//            LocalDateTime.now(), false, "", false);
//        ResponseEntity<CouponDetail> responseEntity =
//            new ResponseEntity(couponDetail, HttpStatus.OK);
//        //mocking
//        ApiEntity<CouponDetail> object = new ApiEntity<>();
//        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
//        when(restService.get(anyString(), any(),
//            ArgumentMatchers.<Class<CouponDetail>>any())).thenReturn(object);
//
//        mockMvc.perform(get(URI_PREFIX + "/update/0").accept(MediaType.TEXT_HTML))
//            .andExpect(status().isOk())
//            .andExpect(result -> result.getModelAndView().getViewName().equals(RETURN_PAGE))
//            .andExpect(result -> result.getModelAndView().getModel().get("targetUrl")
//                .equals("coupon/couponUpdateForm"))
//            .andReturn();
//    }

    @Test
    void postUpdateCouponForm() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("name", "coupon1");
        map.add("typeCode", "1");
        map.add("amount", "1000");
        map.add("isOrderCoupon", "true");
        map.add("applyItemId", "12");
        map.add("issueAmount", "500");
        map.add("minimumUseAmount", "10000");
        map.add("maximumDiscountAmount", "1000");
        map.add("issuanceDeadlineAt", "2030-10-30T12:34");
        map.add("isDuplicatable", "true");
        map.add("isLimited", "true");
        map.add("validateTerm", "10");

        ApiEntity<String> apiEntity = new ApiEntity<>();
        ResponseEntity<String> responseEntity = new ResponseEntity<>("couponList", HttpStatus.OK);

        ReflectionTestUtils.setField(apiEntity, "successResponse", responseEntity);
        //mocking
        when(restService.put(anyString(), anyMap(), ArgumentMatchers.<Class<String>>any()))
            .thenReturn(apiEntity);

        mockMvc.perform(post(URI_PREFIX + "/update-form/0").accept(MediaType.TEXT_HTML)
                .params(map)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> result.getResponse().getRedirectedUrl().equals(""));
    }

    @Test
    void deleteCoupon() throws Exception {
        mockMvc.perform(get(URI_PREFIX + "/delete/0").accept(MediaType.TEXT_HTML))
            .andExpect(status().is3xxRedirection())
            .andExpect(result -> result.getResponse().getRedirectedUrl().equals(""));
    }

    @Test
    void historyCoupon() throws Exception {
        //given
        List<CouponHistoryResponse> couponHistoryResponseList = new ArrayList<>();
        couponHistoryResponseList.add(couponHistoryResponse);

        PageResponse<CouponHistoryResponse> couponHistoryPageResponse = new PageResponse<>();

        ReflectionTestUtils.setField(couponHistoryPageResponse, "pageNumber", 0);
        ReflectionTestUtils.setField(couponHistoryPageResponse, "pageSize", 20);
        ReflectionTestUtils.setField(couponHistoryPageResponse, "totalPages", 0);
        ReflectionTestUtils.setField(couponHistoryPageResponse, "data", couponHistoryResponseList);

        ResponseEntity<PageResponse<CouponHistoryResponse>> responseEntity =
            new ResponseEntity<>(couponHistoryPageResponse, HttpStatus.OK);

        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);

        //when
        when(restService.get((String) any(), any(),
            (ParameterizedTypeReference<Object>) any())).thenReturn(object);

        //then
        mockMvc.perform(get(URI_PREFIX + "/issue-history").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName()
                    .equals("coupon/issue/issueHistoryView"))
            .andReturn();
    }

    @Test
    void historyCouponMember() throws Exception {
        List<CouponHistoryResponse> couponHistoryResponseList = new ArrayList<>();
        couponHistoryResponseList.add(couponHistoryResponse);
        PageResponse<CouponHistoryResponse> couponHistoryPageResponse = new PageResponse<>();

        ReflectionTestUtils.setField(couponHistoryPageResponse, "pageNumber", 0);
        ReflectionTestUtils.setField(couponHistoryPageResponse, "pageSize", 20);
        ReflectionTestUtils.setField(couponHistoryPageResponse, "totalPages", 0);
        ReflectionTestUtils.setField(couponHistoryPageResponse, "data", couponHistoryResponseList);
        ResponseEntity<PageResponse<CouponHistoryResponse>> responseEntity =
                new ResponseEntity<>(couponHistoryPageResponse, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
                (ParameterizedTypeReference<Object>) any())).thenReturn(object);

        //then
        mockMvc.perform(get(URI_PREFIX + "/issue-history/0").accept(MediaType.TEXT_HTML)
                        .param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName()
                        .equals("coupon/issue/issueView"))
                .andReturn();
    }

    @Test
    void memberHistoryCoupon() throws Exception {
        List<CouponHistory> historyList = new ArrayList<>();
        historyList.add(couponHistory);
        PageResponse<CouponHistory> couponHistoryPageResponse = new PageResponse<>();
        ReflectionTestUtils.setField(couponHistoryPageResponse, "data", historyList);
        ResponseEntity<PageResponse<CouponHistory>> responseEntity =
            new ResponseEntity<>(couponHistoryPageResponse, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
            (ParameterizedTypeReference<Object>) any())).thenReturn(object);


        //then
        mockMvc.perform(get(URI_PREFIX + "/history/0/0").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName().equals(RETURN_PAGE))
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getModel().get("targetUrl")
                .equals("coupon/historyView"))
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
        ResponseEntity<List<CouponIssue>> responseEntity =
            new ResponseEntity<>(issueList, HttpStatus.OK);
        //mocking
        ApiEntity<Object> object = new ApiEntity<>();
        ReflectionTestUtils.setField(object, "successResponse", responseEntity);
        when(restService.get(anyString(), any(),
            (ParameterizedTypeReference<Object>) any())).thenReturn(object);


        //then
        mockMvc.perform(get(URI_PREFIX + "/issue").accept(MediaType.TEXT_HTML))
            .andExpect(status().isOk())
            .andExpect(result -> Objects.requireNonNull(result.getModelAndView()).getViewName()
                .equals("coupon/issueView"))
            .andReturn();
    }

}