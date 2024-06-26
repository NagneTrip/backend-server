package com.ssafy.nagne.web.comment;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
class CommentRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("댓글 작성 테스트")
    @WithMockJwtAuthentication
    void saveTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/comments")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"articleId\" : 1, \"content\" : \"newComment\"}")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.commentInfo.id").exists())
                .andExpect(jsonPath("$.response.commentInfo.userId", is(1)))
                .andExpect(jsonPath("$.response.commentInfo.createdDate").exists());
    }

    @Test
    @DisplayName("댓글 작성 실패 테스트 (길이 제한을 초과한 경우)")
    @WithMockJwtAuthentication
    void saveFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/comments")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"articleId\" : 1, \"content\" : \"" + "newComment".repeat(21) + "\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("content must be less than 200 characters")));
    }

    @Test
    @DisplayName("댓글 조회 테스트")
    @WithMockJwtAuthentication
    void findByIdTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/comments/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.commentInfo.id", is(1)))
                .andExpect(jsonPath("$.response.commentInfo.articleId", is(1)))
                .andExpect(jsonPath("$.response.commentInfo.userId", is(1)))
                .andExpect(jsonPath("$.response.commentInfo.content", is("1번 게시글 유저1의 댓글")))
                .andExpect(jsonPath("$.response.commentInfo.likeCount", is(1)))
                .andExpect(jsonPath("$.response.commentInfo.isLiked", is(true)))
                .andExpect(jsonPath("$.response.commentInfo.createdDate").exists());
    }

    @Test
    @DisplayName("게시글 별 댓글 목록 조회 테스트")
    @WithMockJwtAuthentication(id = 2L)
    void findCommentsByArticleIdTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/comments")
                        .param("articleId", "1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("findCommentsByArticleId"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.comments", hasSize(3)));
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    @WithMockJwtAuthentication
    void updateTest() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/comments/1")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"content\" : \"updateComment\"}")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("댓글 수정 실패 테스트 (길이 제한을 초과한 경우)")
    @WithMockJwtAuthentication
    void updateFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/comments/1")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"content\" : \"" + "newComment".repeat(21) + "\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("content must be less than 200 characters")));
    }

    @Test
    @DisplayName("댓글 수정 실패 테스트 (다른 사람의 댓글을 수정하는 경우)")
    @WithMockJwtAuthentication(id = 2L)
    void updateFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/comments/1")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"content\" : \"updateComment\"}")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(403)))
                .andExpect(jsonPath("$.error.message", is("Forbidden")));
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    @WithMockJwtAuthentication
    void deleteTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/comments/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("댓글 삭제 실패 테스트 (다른 사람의 댓글을 삭제하는 경우)")
    @WithMockJwtAuthentication(id = 2L)
    void deleteFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/comments/1")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(CommentRestController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(403)))
                .andExpect(jsonPath("$.error.message", is("Forbidden")));
    }
}