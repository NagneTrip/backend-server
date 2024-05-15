package com.ssafy.nagne.security;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class JwtLoginFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"username\" : \"test1@gmail.com\", \"password\" : \"1234\"}")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.token").exists())
                .andExpect(jsonPath("$.response.token").isString())
                .andExpect(jsonPath("$.response.userInfo.id", is(1)))
                .andExpect(jsonPath("$.response.userInfo.username", is("test1@gmail.com")))
                .andExpect(jsonPath("$.response.userInfo.lastLoginDate").exists());
    }

    @Test
    @DisplayName("로그인 실패 테스트 (아이디가 올바르지 않은 경우)")
    void wrongUsernameLoginFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"username\" : \"wrongUsername\", \"password\" : \"1234\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)))
                .andExpect(jsonPath("$.error.message", is("Bad Credential")));
    }

    @Test
    @DisplayName("로그인 실패 테스트 (비밀번호가 올바르지 않은 경우)")
    void wrongPasswordLoginFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"username\" : \"test1@gmail.com\", \"password\" : \"wrongPassword\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(401)))
                .andExpect(jsonPath("$.error.message", is("Bad Credential")));
    }

    @Test
    @DisplayName("로그인 실패 테스트 (아이디가 누락된 경우)")
    void omitUsernameLoginFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"password\" : \"1234\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("username must be provided")));
    }

    @Test
    @DisplayName("로그인 실패 테스트 (비밀번호가 누락된 경우)")
    void omitPasswordLoginFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/users/login")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"username\" : \"test1@gmail.com\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("password must be provided")));
    }
}