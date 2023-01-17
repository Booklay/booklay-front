package com.nhnacademy.booklay.booklayfront.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@WebMvcTest(MemberController.class)
@ActiveProfiles("test")
class MemberControllerTest {

    @MockBean
    RestTemplate restTemplate;

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("멤버 생성 테스트")
    void testCreateMember() {
        //given


    }

}