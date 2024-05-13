package com.ssafy.nagne.web.article;

import static org.hamcrest.Matchers.hasSize;
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
    @DisplayName("게시글 작성 실패 테스트 (길이 제한을 초과할 경우)")
    @WithMockJwtAuthentication
    void saveFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(POST, "/api/articles")
                        .part(new MockPart(
                                "request",
                                "request",
                                ("{\"content\" : \"" + "A".repeat(1001) + "\"}").getBytes(),
                                MediaType.APPLICATION_JSON
                        ))
                        .file(new MockMultipartFile("images", "image1".getBytes()))
                        .file(new MockMultipartFile("images", "image2".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("content must be less than 1,000 characters")));
    }

    @Test
    @DisplayName("게시글 작성 실패 테스트 (최대 허용 사진 개수를 초과한 경우)")
    @WithMockJwtAuthentication
    void saveFailureTest2() throws Exception {
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
                        .file(new MockMultipartFile("images", "image3".getBytes()))
                        .file(new MockMultipartFile("images", "image4".getBytes()))
                        .file(new MockMultipartFile("images", "image5".getBytes()))
                        .file(new MockMultipartFile("images", "image6".getBytes()))
                        .file(new MockMultipartFile("images", "image7".getBytes()))
                        .file(new MockMultipartFile("images", "image8".getBytes()))
                        .file(new MockMultipartFile("images", "image9".getBytes()))
                        .file(new MockMultipartFile("images", "image10".getBytes()))
                        .file(new MockMultipartFile("images", "image11".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("must be 10 images or less")));
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
                .andExpect(jsonPath("$.response.articleInfo.content", is("#태그1 김두열1의 글")))
                .andExpect(jsonPath("$.response.articleInfo.likeCount", is(1)))
                .andExpect(jsonPath("$.response.articleInfo.isLiked", is(true)))
                .andExpect(jsonPath("$.response.articleInfo.isBookmarked", is(true)))
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

    @Test
    @DisplayName("해시태그 기반 게시글 목록 조회 테스트1")
    @WithMockJwtAuthentication
    void findArticlesTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/tags")
                        .param("tags", "#태그1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findArticlesByTags"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(4)));
    }

    @Test
    @DisplayName("해시태그 기반 게시글 목록 조회 테스트2")
    @WithMockJwtAuthentication
    void findArticlesTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/tags")
                        .param("tags", "#태그1", "#태그2")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findArticlesByTags"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(5)));
    }

    @Test
    @DisplayName("해시태그 기반 게시글 목록 조회 테스트3")
    @WithMockJwtAuthentication
    void findArticlesTest3() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/tags")
                        .param("tags", "#태그1", "#태그2")
                        .param("size", "3")
                        .param("lastIndex", "5")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findArticlesByTags"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(3)))
                .andExpect(jsonPath("$.response.articles[0].id", is(3)));
    }

    @Test
    @DisplayName("해시태그 기반 게시글 목록 조회 테스트4")
    @WithMockJwtAuthentication
    void findArticlesTest4() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/tags")
                        .param("tags", "#태그1", "#태그2", "태그3", "태그4", "태그5")
                        .param("size", "3")
                        .param("lastIndex", "1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findArticlesByTags"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(0)));
    }

    @Test
    @DisplayName("팔로워 게시글 목록 조회 테스트1")
    @WithMockJwtAuthentication
    void findFollowerArticlesTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/followers")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findFollowerArticles"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(7)));
    }

    @Test
    @DisplayName("팔로워 게시글 목록 조회 테스트2")
    @WithMockJwtAuthentication(id = 2L)
    void findFollowerArticlesTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/followers")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findFollowerArticles"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(3)));
    }

    @Test
    @DisplayName("팔로워 게시글 목록 조회 테스트3")
    @WithMockJwtAuthentication
    void findFollowerArticlesTest3() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/followers")
                        .param("size", "3")
                        .param("lastIndex", "7")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findFollowerArticles"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(3)))
                .andExpect(jsonPath("$.response.articles", hasSize(3)))
                .andExpect(jsonPath("$.response.articles[0].id", is(6)));
    }

    @Test
    @DisplayName("북마크 게시글 목록 조회 테스트1")
    @WithMockJwtAuthentication
    void findBookmarkArticlesTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/bookmark")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findBookmarkArticles"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(5)));
    }

    @Test
    @DisplayName("북마크 게시글 목록 조회 테스트2")
    @WithMockJwtAuthentication(id = 2L)
    void findBookmarkArticlesTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/bookmark")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findBookmarkArticles"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(0)));
    }

    @Test
    @DisplayName("북마크 게시글 목록 조회 테스트3")
    @WithMockJwtAuthentication
    void findBookmarkArticlesTest3() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/articles/bookmark")
                        .param("size", "2")
                        .param("lastIndex", "5")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("findBookmarkArticles"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.articles", hasSize(2)))
                .andExpect(jsonPath("$.response.articles[0].id", is(4)));
    }

    @Test
    @DisplayName("게시글 수정 성공 테스트")
    @WithMockJwtAuthentication
    void updateSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(PATCH, "/api/articles/1")
                        .part(new MockPart("request", "request",
                                ("{\"content\" : \"newContent\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("images", "image1".getBytes()))
                        .file(new MockMultipartFile("images", "image2".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("게시글 수정 실패 테스트 (길이 제한을 초과할 경우)")
    @WithMockJwtAuthentication
    void updateFailureTest1() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(PATCH, "/api/articles/1")
                        .part(new MockPart(
                                "request",
                                "request",
                                ("{\"content\" : \"" + "A".repeat(1001) + "\"}").getBytes(),
                                MediaType.APPLICATION_JSON
                        ))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("content must be less than 1,000 characters")));
    }

    @Test
    @DisplayName("게시글 수정 실패 테스트 (최대 허용 사진 개수를 초과한 경우)")
    @WithMockJwtAuthentication
    void updateFailureTest2() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(PATCH, "/api/articles/1")
                        .part(new MockPart(
                                "request",
                                "request",
                                "{\"content\" : \"오늘은 #맑은날씨 입니다. #해시태그\"}".getBytes(),
                                MediaType.APPLICATION_JSON
                        ))
                        .file(new MockMultipartFile("images", "image1".getBytes()))
                        .file(new MockMultipartFile("images", "image2".getBytes()))
                        .file(new MockMultipartFile("images", "image3".getBytes()))
                        .file(new MockMultipartFile("images", "image4".getBytes()))
                        .file(new MockMultipartFile("images", "image5".getBytes()))
                        .file(new MockMultipartFile("images", "image6".getBytes()))
                        .file(new MockMultipartFile("images", "image7".getBytes()))
                        .file(new MockMultipartFile("images", "image8".getBytes()))
                        .file(new MockMultipartFile("images", "image9".getBytes()))
                        .file(new MockMultipartFile("images", "image10".getBytes()))
                        .file(new MockMultipartFile("images", "image11".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(400)))
                .andExpect(jsonPath("$.error.message", is("must be 10 images or less")));
    }

    @Test
    @DisplayName("게시글 수정 실패 테스트 (다른 사람의 게시글을 수정하는 경우)")
    @WithMockJwtAuthentication(id = 2L)
    void updateFailureTest3() throws Exception {
        ResultActions result = mockMvc.perform(
                multipart(PATCH, "/api/articles/1")
                        .part(new MockPart("request", "request",
                                ("{\"content\" : \"newContent\"}").getBytes(),
                                APPLICATION_JSON))
                        .file(new MockMultipartFile("images", "image1".getBytes()))
                        .file(new MockMultipartFile("images", "image2".getBytes()))
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(403)))
                .andExpect(jsonPath("$.error.message", is("Forbidden")));
    }

    @Test
    @DisplayName("게시글 삭제 성공 테스트")
    @WithMockJwtAuthentication
    void deleteSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/articles/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("게시글 삭제 실패 테스트")
    @WithMockJwtAuthentication(id = 2L)
    void deleteFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/articles/1")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(ArticleRestController.class))
                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(403)))
                .andExpect(jsonPath("$.error.message", is("Forbidden")));
    }
}