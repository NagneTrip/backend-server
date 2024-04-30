package com.ssafy.nagne.web.attraction;

import static com.ssafy.nagne.utils.ApiUtils.success;

import com.ssafy.nagne.service.AttractionService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/attractions")
@RequiredArgsConstructor
public class AttractionRestController {

    private final AttractionService attractionService;

    @GetMapping("/{id}")
    public ApiResult<AttractionDetailResult> findById(@PathVariable Long id) {
        return success(new AttractionDetailResult(attractionService.findById(id)));
    }

    @GetMapping
    public ApiResult<AttractionListResult> findAttractionsByKeyword(@RequestParam String keyword) {
        return success(new AttractionListResult(attractionService.findAttractionsByKeyword(keyword)));
    }
}
