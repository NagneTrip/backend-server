package com.ssafy.nagne.web.user;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("회원가입 성공 테스트")
    void joinSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart("/api/users/me")
                        .part(new MockPart("request", "request",
                                ("{\"username\" : \"newUser@gmail.com\", \"password\" : \"1234\", "
                                 + "\"nickname\" : \"newUser\", \"phone\" : \"01059220969\", \"gender\" : \"MAN\", "
                                 + "\"birth\" : \"1998-05-04\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(MeRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.userInfo.id").exists())
                .andExpect(jsonPath("$.response.userInfo.username", is("newUser@gmail.com")))
                .andExpect(jsonPath("$.response.userInfo.nickname", is("newUser")))
                .andExpect(jsonPath("$.response.userInfo.createdDate").exists());
    }

    @Test
    @DisplayName("회원가입 실패 테스트 (아이디가 누락된 경우)")
    void omitUsernameJoinFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart("/api/users/me")
                        .part(new MockPart("request", "request",
                                ("{\"password\" : \"1234\", "
                                 + "\"nickname\" : \"newUser\", \"phone\" : \"01000000000\", \"gender\" : \"MAN\", "
                                 + "\"birth\" : \"1990-01-01\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("username must be provided")));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 (비밀번호가 누락된 경우)")
    void omitPasswordJoinFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart("/api/users/me")
                        .part(new MockPart("request", "request",
                                ("{\"username\" : \"newUser@gmail.com\", "
                                 + "\"nickname\" : \"newUser\", \"phone\" : \"01000000000\", \"gender\" : \"MAN\", "
                                 + "\"birth\" : \"1990-01-01\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("password must be provided")));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 (닉네임이 누락된 경우)")
    void omitNicknameJoinFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart("/api/users/me")
                        .part(new MockPart("request", "request",
                                ("{\"username\" : \"newUser@gmail.com\", \"password\" : \"1234\", "
                                 + "\"phone\" : \"01000000000\", \"gender\" : \"MAN\", "
                                 + "\"birth\" : \"1990-01-01\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("nickname must be provided")));
    }

    @Test
    @DisplayName("회원가입 실패 테스트 (아이디가 이메일 형식이 아닌 경우)")
    void WrongFormUsernameJoinFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart("/api/users/me")
                        .part(new MockPart("request", "request",
                                ("{\"username\" : \"newUser\", \"password\" : \"1234\", "
                                 + "\"nickname\" : \"newUser\", \"phone\" : \"01059220969\", \"gender\" : \"MAN\", "
                                 + "\"birth\" : \"1998-05-04\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(MeRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("username must be a well-formed email address")));
    }
}