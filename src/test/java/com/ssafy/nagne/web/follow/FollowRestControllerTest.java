package com.ssafy.nagne.web.follow;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class FollowRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("팔로우 테스트")
    @WithMockJwtAuthentication
    void followTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/follow")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"followId\" : \"3\"}")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(FollowRestController.class))
                .andExpect(handler().methodName("follow"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("팔로우 실패 테스트 (이미 팔로우한 경우)")
    @WithMockJwtAuthentication
    void followFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/follow")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"followId\" : \"2\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(FollowRestController.class))
                .andExpect(handler().methodName("follow"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("already followed")));
    }

    @Test
    @DisplayName("팔로우 실패 테스트 (없는 유저를 팔로우한 경우)")
    @WithMockJwtAuthentication
    void followFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/follow")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"followId\" : \"1000\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(FollowRestController.class))
                .andExpect(handler().methodName("follow"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)))
                .andExpect(jsonPath("$.error.message", is("Could not found user for 1000")));
    }

    @Test
    @DisplayName("팔로우 체크 테스트 (팔로우한 경우)")
    @WithMockJwtAuthentication
    void checkTrueTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/follow/2")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(FollowRestController.class))
                .andExpect(handler().methodName("check"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.checkFollow", is(true)));
    }

    @Test
    @DisplayName("팔로우 체크 테스트 (팔로우 안한 경우)")
    @WithMockJwtAuthentication
    void checkFalseTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/follow/3")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(FollowRestController.class))
                .andExpect(handler().methodName("check"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.checkFollow", is(false)));
    }

    @Test
    @DisplayName("언팔로우 테스트")
    @WithMockJwtAuthentication
    void unfollowTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/follow/2")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(FollowRestController.class))
                .andExpect(handler().methodName("unfollow"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("언팔로우 실패 테스트 (이미 언팔로우한 경우)")
    @WithMockJwtAuthentication
    void unfollowFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/follow/3")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(FollowRestController.class))
                .andExpect(handler().methodName("unfollow"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("already unfollowed")));
    }
}