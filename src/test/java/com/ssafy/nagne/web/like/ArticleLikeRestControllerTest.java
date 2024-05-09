package com.ssafy.nagne.web.like;

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
class ArticleLikeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("게시글 좋아요 테스트")
    @WithMockJwtAuthentication
    void likeTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/articles/like")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"articleId\" : \"3\"}")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleLikeRestController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("게시글 좋아요 실패 테스트 (이미 좋아요한 경우)")
    @WithMockJwtAuthentication
    void likeFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/articles/like")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"articleId\" : \"2\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleLikeRestController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("already liked")));
    }

    @Test
    @DisplayName("게시글 좋아요 실패 테스트 (없는 게시글에 좋아요한 경우)")
    @WithMockJwtAuthentication
    void likeFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/articles/like")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"articleId\" : \"1000\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleLikeRestController.class))
                .andExpect(handler().methodName("like"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)))
                .andExpect(jsonPath("$.error.message", is("Could not found article for 1000")));
    }

    @Test
    @DisplayName("게시글 좋아요 체크 테스트 (좋아요한 경우)")
    @WithMockJwtAuthentication
    void checkTrueTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/like/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleLikeRestController.class))
                .andExpect(handler().methodName("check"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.checkLike", is(true)));
    }

    @Test
    @DisplayName("게시글 좋아요 체크 테스트 (좋아요 안한 경우)")
    @WithMockJwtAuthentication
    void checkFalseTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/like/3")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleLikeRestController.class))
                .andExpect(handler().methodName("check"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.checkLike", is(false)));
    }

    @Test
    @DisplayName("게시글 좋아요 취소 테스트")
    @WithMockJwtAuthentication
    void unlikeTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/articles/like/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleLikeRestController.class))
                .andExpect(handler().methodName("unlike"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("게시글 좋아요 취소 실패 테스트 (이미 좋아요 취소한 경우)")
    @WithMockJwtAuthentication
    void unlikeFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/articles/like/3")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleLikeRestController.class))
                .andExpect(handler().methodName("unlike"))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("already unliked")));
    }
}