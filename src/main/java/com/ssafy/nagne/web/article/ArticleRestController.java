package com.ssafy.nagne.web.article;

import static com.ssafy.nagne.utils.ApiUtils.success;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.error.NotFoundException;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.ArticleService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/articles")
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    @PostMapping
    public ApiResult<SaveResult> save(@AuthenticationPrincipal JwtAuthentication authentication,
                                      @RequestBody SaveRequest request) {
        return success(new SaveResult(articleService.save(article(request, authentication.id()))));
    }

    @GetMapping("/{id}")
    public ApiResult<ArticleDetailResult> findById(@PathVariable Long id) {
        return success(new ArticleDetailResult(
                articleService.findById(id)
                        .orElseThrow(() -> new NotFoundException(
                                "Could not found article for " + id)))
        );
    }

    private Article article(SaveRequest request, Long userId) {
        return Article.builder()
                .userId(userId)
                .title(request.title())
                .content(request.content())
                .createdDate(now())
                .build();
    }
}
