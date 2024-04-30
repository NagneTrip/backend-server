package com.ssafy.nagne.web.travelplan;

import static com.ssafy.nagne.utils.ApiUtils.success;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.TravelPlan;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.TravelPlanService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/travel-plans")
@RequiredArgsConstructor
public class TravelPlanRestController {

    private final TravelPlanService travelPlanService;

    @PostMapping
    public ApiResult<SaveResult> save(@AuthenticationPrincipal JwtAuthentication authentication,
                                      @Valid @RequestBody SaveRequest request) {
        log.info("request={}", request);

        return success(
                new SaveResult(travelPlanService.save(travelPlan(authentication.id()), request.attractions()))
        );
    }

    @GetMapping("/{id}")
    public ApiResult<TravelPlanDetailResult> findById(@PathVariable Long id) {
        return success(new TravelPlanDetailResult(travelPlanService.findById(id)));
    }

    @DeleteMapping("/{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        return success(travelPlanService.delete(id));
    }

    private TravelPlan travelPlan(Long userId) {
        return TravelPlan.builder()
                .userId(userId)
                .createdDate(now())
                .build();
    }
}
