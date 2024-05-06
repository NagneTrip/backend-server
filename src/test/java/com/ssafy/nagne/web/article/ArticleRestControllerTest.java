package com.ssafy.nagne.web.article;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpMethod.POST;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
class ArticleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("게시글 작성 테스트")
    @WithMockJwtAuthentication
    void saveTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(POST, "/api/articles")
                        .part(new MockPart(
                                "request",
                                "request",
                                "{\"content\" : \"오늘은 #맑은날씨 입니다. #해시태그\"}".getBytes(),
                                MediaType.APPLICATION_JSON
                        ))
                        .file(new MockMultipartFile("images", "image1".getBytes()))
                        .file(new MockMultipartFile("images", "image2".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articleInfo.id").exists())
                .andExpect(jsonPath("$.response.articleInfo.userId", is(1)))
                .andExpect(jsonPath("$.response.articleInfo.createdDate").exists());
    }

    @Test
    @DisplayName("이미지 없는 게시글 작성 테스트")
    @WithMockJwtAuthentication
    void saveWithoutImageTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(POST, "/api/articles")
                        .part(new MockPart(
                                "request",
                                "request",
                                "{\"content\" : \"오늘은 #맑은날씨 입니다. #해시태그\"}".getBytes(),
                                MediaType.APPLICATION_JSON
                        ))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articleInfo.id").exists())
                .andExpect(jsonPath("$.response.articleInfo.userId", is(1)))
                .andExpect(jsonPath("$.response.articleInfo.createdDate").exists());
    }

    @Test
    @DisplayName("해시태그 없는 게시글 작성 테스트")
    @WithMockJwtAuthentication
    void saveWithoutHashTagTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(POST, "/api/articles")
                        .part(new MockPart(
                                "request",
                                "request",
                                "{\"content\" : \"해시태그 없는 게시물\"}".getBytes(),
                                MediaType.APPLICATION_JSON
                        ))
                        .file(new MockMultipartFile("images", "image1".getBytes()))
                        .file(new MockMultipartFile("images", "image2".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articleInfo.id").exists())
                .andExpect(jsonPath("$.response.articleInfo.userId", is(1)))
                .andExpect(jsonPath("$.response.articleInfo.createdDate").exists());
    }

    @Test
    @DisplayName("게시글 조회 성공 테스트")
    @WithMockJwtAuthentication
    void findByIdSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articleInfo.id", is(1)))
                .andExpect(jsonPath("$.response.articleInfo.userId", is(1)))
                .andExpect(jsonPath("$.response.articleInfo.content", is("김두열1의 글")))
                .andExpect(jsonPath("$.response.articleInfo.good", is(0)))
                .andExpect(jsonPath("$.response.articleInfo.imageUrls").exists())
                .andExpect(jsonPath("$.response.articleInfo.createdDate").exists());
    }

    @Test
    @DisplayName("게시글 조회 실패 테스트 (없는 게시글 ID로 접근하는 경우)")
    @WithMockJwtAuthentication
    void findByIdFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/0")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)))
                .andExpect(jsonPath("$.error.message", is("Could not found article for 0")));
    }
}