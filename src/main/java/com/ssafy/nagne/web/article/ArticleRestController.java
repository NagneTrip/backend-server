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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    //TODO: 페이징 처리
    @GetMapping
    public ApiResult<ArticleListResult> findAll() {
        return success(new ArticleListResult(articleService.findAll()));
    }

    //TODO: 글 작성자만 변경 가능하게 하기
    @PutMapping("/{id}")
    public ApiResult<Boolean> update(@PathVariable Long id, @RequestBody UpdateRequest updateRequest) {
        return success(articleService.update(id, article(updateRequest)));
    }

    //TODO: 글 작성자만 삭제 가능하게 하기
    @DeleteMapping("/{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        return success(articleService.delete(id));
    }

    private Article article(SaveRequest request, Long userId) {
        return Article.builder()
                .userId(userId)
                .title(request.title())
                .content(request.content())
                .createdDate(now())
                .build();
    }

    private Article article(UpdateRequest request) {
        return Article.builder()
                .title(request.title())
                .content(request.content())
                .build();
    }
}
