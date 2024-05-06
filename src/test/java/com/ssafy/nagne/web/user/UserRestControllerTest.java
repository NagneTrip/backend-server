package com.ssafy.nagne.web.user;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ssafy.nagne.security.WithMockJwtAuthentication;
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
                multipart(POST, "/api/users")
                        .part(new MockPart("request", "request",
                                ("{\"username\" : \"newUser@gmail.com\", \"password\" : \"1234\", "
                                        + "\"nickname\" : \"newUser\", \"phone\" : \"01059220969\", \"gender\" : \"MAN\", "
                                        + "\"birth\" : \"1998-05-04\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
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
                multipart("/api/users")
                        .part(new MockPart("request", "request",
                                ("{\"password\" : \"1234\", "
                                        + "\"nickname\" : \"newUser\", \"phone\" : \"01000000000\", \"gender\" : \"MAN\", "
                                        + "\"birth\" : \"1990-01-01\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserRestController.class))
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
                multipart("/api/users")
                        .part(new MockPart("request", "request",
                                ("{\"username\" : \"newUser@gmail.com\", "
                                        + "\"nickname\" : \"newUser\", \"phone\" : \"01000000000\", \"gender\" : \"MAN\", "
                                        + "\"birth\" : \"1990-01-01\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserRestController.class))
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
                multipart("/api/users")
                        .part(new MockPart("request", "request",
                                ("{\"username\" : \"newUser@gmail.com\", \"password\" : \"1234\", "
                                        + "\"phone\" : \"01000000000\", \"gender\" : \"MAN\", "
                                        + "\"birth\" : \"1990-01-01\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserRestController.class))
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
                multipart("/api/users")
                        .part(new MockPart("request", "request",
                                ("{\"username\" : \"newUser\", \"password\" : \"1234\", "
                                        + "\"nickname\" : \"newUser\", \"phone\" : \"01059220969\", \"gender\" : \"MAN\", "
                                        + "\"birth\" : \"1998-05-04\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("username must be a well-formed email address")));
    }

    @Test
    @DisplayName("유저 조회 성공 테스트")
    @WithMockJwtAuthentication
    void findByIdSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.userInfo.id", is(1)))
                .andExpect(jsonPath("$.response.userInfo.username", is("test1@gmail.com")))
                .andExpect(jsonPath("$.response.userInfo.nickname", is("김두열1")))
                .andExpect(jsonPath("$.response.userInfo.tier", is("UNRANKED")));
    }

    @Test
    @DisplayName("유저 조회 실패 테스트 (없는 유저 ID로 접근하는 경우)")
    @WithMockJwtAuthentication
    void findByIdFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/0")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)))
                .andExpect(jsonPath("$.error.message", is("Could not found user for 0")));
    }

    @Test
    @DisplayName("유저 조회 (자세히) 성공 테스트")
    @WithMockJwtAuthentication
    void findByIdDetailSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/1/detail")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("findByIdDetail"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.userInfo.id", is(1)))
                .andExpect(jsonPath("$.response.userInfo.username", is("test1@gmail.com")))
                .andExpect(jsonPath("$.response.userInfo.nickname", is("김두열1")))
                .andExpect(jsonPath("$.response.userInfo.phone", is("01011111111")))
                .andExpect(jsonPath("$.response.userInfo.birth", is("1998-05-04")))
                .andExpect(jsonPath("$.response.userInfo.gender", is("MAN")))
                .andExpect(jsonPath("$.response.userInfo.tier", is("UNRANKED")))
                .andExpect(jsonPath("$.response.userInfo.createdDate").exists());
    }

    @Test
    @DisplayName("유저 조회 (자세히) 실패 테스트 (다른 사람의 정보에 접근하는 경우)")
    @WithMockJwtAuthentication(id = 2L)
    void findByIdDetailFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/1/detail")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("findByIdDetail"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(403)));
    }

    @Test
    @DisplayName("키워드 기반 유저 목록 조회 테스트")
    @WithMockJwtAuthentication
    void findAllTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users?keyword=test")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("findUsers"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("유저 팔로워 목록 조회 테스트")
    @WithMockJwtAuthentication
    void findFollowersTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/1/followers")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("findFollowers"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("유저 팔로잉 목록 조회 테스트")
    @WithMockJwtAuthentication
    void findFollowingsTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/users/1/followings")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("findFollowings"))
                .andExpect(jsonPath("$.success", is(true)));
    }

    @Test
    @DisplayName("유저 정보 수정 성공 테스트")
    @WithMockJwtAuthentication
    void updateSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(PATCH, "/api/users/1")
                        .part(new MockPart("request", "request",
                                ("{\"nickname\" : \"newNickname\", \"phone\" : \"010-1234-5678\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("유저 정보 수정 실패 테스트 (다른 사람의 정보를 수정하는 경우)")
    @WithMockJwtAuthentication(id = 2L)
    void updateFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(PATCH, "/api/users/1")
                        .part(new MockPart("request", "request",
                                ("{\"nickname\" : \"newNickname\", \"phone\" : \"010-1234-5678\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("profileImage", "profileImage".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(403)));
    }

    @Test
    @DisplayName("유저 삭제 성공 테스트")
    @WithMockJwtAuthentication
    void deleteSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/users/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("유저 삭제 실패 테스트 (다른 유저를 삭제 하는 경우)")
    @WithMockJwtAuthentication(id = 2L)
    void deleteFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/users/1")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(UserRestController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(403)));
    }
}