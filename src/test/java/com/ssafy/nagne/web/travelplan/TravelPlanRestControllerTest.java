package com.ssafy.nagne.web.travelplan;

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
class TravelPlanRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("여행 계획 작성 테스트")
    @WithMockJwtAuthentication
    void saveTest() throws Exception {
        ResultActions result = mockMvc.perform(
                post("/api/travel-plans")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"attractions\" : [125266, 125405]}")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TravelPlanRestController.class))
                .andExpect(handler().methodName("save"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.travelPlanInfo.id").exists())
                .andExpect(jsonPath("$.response.travelPlanInfo.userId", is(1)))
                .andExpect(jsonPath("$.response.travelPlanInfo.createdDate").exists());
    }

    @Test
    @DisplayName("여행 계획 조회 테스트")
    @WithMockJwtAuthentication
    void findByIdTest() throws Exception {
        ResultActions result = mockMvc.perform(
                get("/api/travel-plans/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TravelPlanRestController.class))
                .andExpect(handler().methodName("findById"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response.travelPlanInfo.id", is(1)))
                .andExpect(jsonPath("$.response.travelPlanInfo.userId", is(1)))
                .andExpect(jsonPath("$.response.travelPlanInfo.createdDate").exists());
    }

    @Test
    @DisplayName("여행 계획 수정 테스트")
    @WithMockJwtAuthentication
    void updateTest() throws Exception {
        ResultActions result = mockMvc.perform(
                patch("/api/travel-plans/1")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content("{\"attractions\" : [125266, 125405, 125406]}")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TravelPlanRestController.class))
                .andExpect(handler().methodName("update"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }

    @Test
    @DisplayName("여행 계획 삭제 테스트")
    @WithMockJwtAuthentication
    void deleteTest() throws Exception {
        ResultActions result = mockMvc.perform(
                delete("/api/travel-plans/1")
        );

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(TravelPlanRestController.class))
q                .andExpect(handler().methodName("delete"))
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.response", is(true)));
    }
}