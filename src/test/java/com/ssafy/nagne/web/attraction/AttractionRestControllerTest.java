package com.ssafy.nagne.web.attraction;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
class AttractionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("여행지 조회 성공 테스트")
    @WithMockJwtAuthentication
    void findByIdSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/attractions/125266")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(AttractionRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.attractionInfo.id", is(125266)))
                .andExpect(jsonPath("$.response.attractionInfo.sidoCode", is(32)))
                .andExpect(jsonPath("$.response.attractionInfo.gugunCode", is(18)))
                .andExpect(jsonPath("$.response.attractionInfo.title", is("국립 청태산자연휴양림")))
                .andExpect(jsonPath("$.response.attractionInfo.addr", is("강원도 횡성군 둔내면 청태산로 610")))
                .andExpect(jsonPath("$.response.attractionInfo.zipCode", is("25261")))
                .andExpect(jsonPath("$.response.attractionInfo.tel").isEmpty())
                .andExpect(jsonPath("$.response.attractionInfo.imageUrl").exists())
                .andExpect(jsonPath("$.response.attractionInfo.latitude", is(37.52251412)))
                .andExpect(jsonPath("$.response.attractionInfo.longitude", is(128.2919115)));
    }

    @Test
    @DisplayName("여행지 조회 실패 테스트 (없는 여행지 ID로 접근하는 경우)")
    @WithMockJwtAuthentication
    void findByIdFailureTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/attractions/0")
        );

        result.andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(handler().handlerType(AttractionRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(false)))
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.error.status", is(404)))
                .andExpect(jsonPath("$.error.message", is("Could not found attraction for 0")));
    }

    @Test
    @DisplayName("키워드 기반 여행지 목록 조회 테스트")
    @WithMockJwtAuthentication
    void findAttractionsByKeywordSuccessTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/attractions?keyword=휴양림")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(AttractionRestController.class))
                .andExpect(handler().methodName("findAttractionsByKeyword"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.attractions", hasSize(3)));
    }
}