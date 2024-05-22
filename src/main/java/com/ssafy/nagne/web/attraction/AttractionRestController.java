package com.ssafy.nagne.web.attraction;

import com.ssafy.nagne.page.Pageable;
import com.ssafy.nagne.service.AttractionService;
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
    public AttractionDetailResult findById(@PathVariable Long id) {
        return new AttractionDetailResult(attractionService.findById(id));
    }

    @GetMapping
    public AttractionListResult findAttractionsByKeyword(@RequestParam String keyword,
                                                         @RequestParam Long attractionTypeId,
                                                         Pageable pageable) {
        return new AttractionListResult(
                attractionService.findAttractionsByKeyword(keyword, attractionTypeId, pageable));
    }
}
