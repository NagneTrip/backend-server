package com.ssafy.nagne.web.travelplan;

import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.TravelPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public SaveResult save(@AuthenticationPrincipal JwtAuthentication authentication,
                           @Valid @RequestBody SaveUpdateRequest request) {
        return new SaveResult(travelPlanService.save(request.attractions(), authentication.id()));
    }

    @GetMapping("/{id}")
    public TravelPlanDetailResult findById(@PathVariable Long id) {
        return new TravelPlanDetailResult(travelPlanService.findById(id));
    }

    @PatchMapping("/{id}")
    public Boolean update(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication authentication,
                          @Valid @RequestBody SaveUpdateRequest request) {
        return travelPlanService.update(id, authentication.id(), request.attractions());
    }


    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication authentication) {
        return travelPlanService.delete(id, authentication.id());
    }
}
