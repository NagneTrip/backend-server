package com.ssafy.nagne.web.travelplan;

import static com.ssafy.nagne.utils.ApiUtils.success;

import com.ssafy.nagne.service.TravelPlanService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/travel-plans")
@RequiredArgsConstructor
public class TravelPlanRestController {

    private final TravelPlanService travelPlanService;

    @GetMapping("/{id}")
    public ApiResult<TravelPlanDetailResult> findById(@PathVariable Long id) {
        return success(new TravelPlanDetailResult(travelPlanService.findById(id)));
    }
}
