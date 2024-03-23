package com.eum.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class UserControllerSuccessTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    @DisplayName("유저 로그인 테스트")
    void signInTest() throws  Exception{
        Map<String, String> input = new HashMap<>();
        input.put("email","test3@email");
        input.put("password","1234");
        mockMvc.perform(RestDocumentationRequestBuilders.post("/auth-service/api/v2/auth/signin/local")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-signin",
                        requestFields(
                                fieldWithPath("email").description("로그인 이메일"),
                                fieldWithPath("password").description("로그인 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("status").description("토큰 타입 = bearer"),
                                fieldWithPath("code").description("토큰 타입 = bearer"),
                                fieldWithPath("msg").description("토큰 타입 = bearer"),
                                fieldWithPath("detailMsg").description("토큰 타입 = bearer"),
                                fieldWithPath("data.grantType").description("토큰 타입 = bearer"),
                                fieldWithPath("data.accessToken").description("엑세스 토큰, 프론트에서 헤더에 넣을 토큰"),
                                fieldWithPath("data.refreshToken").description("리프레시 토큰, 토큰 갱신용"),
                                fieldWithPath("data.refreshTokenExpirationTime").description("리프레시 토큰 만료 시간"),
                                fieldWithPath("data.role").description("유저 권한")
                        )

                ))
                .andExpect(jsonPath("$.data.accessToken",is(notNullValue())));

    }
}
