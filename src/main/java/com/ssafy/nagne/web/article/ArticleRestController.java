package com.ssafy.nagne.web.article;

import com.ssafy.nagne.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/articles")
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;
}
