package com.ssafy.nagne.web.article;

import static com.ssafy.nagne.utils.ApiUtils.success;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.page.PageParameter;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.ArticleService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/articles")
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    @PostMapping
    public ApiResult<SaveResult> save(@AuthenticationPrincipal JwtAuthentication authentication,
                                      @RequestPart SaveRequest request,
                                      @RequestPart List<MultipartFile> images) {
        return success(
                new SaveResult(articleService.save(article(request, authentication.id()), images))
        );
    }

    @GetMapping("/{id}")
    public ApiResult<ArticleDetailResult> findById(@PathVariable Long id) {
        return success(new ArticleDetailResult(articleService.findById(id)));
    }

    @GetMapping
    public ApiResult<ArticleListResult> findAll(PageParameter pageParameter) {
        return success(new ArticleListResult(articleService.findAll(pageParameter)));
    }

    @GetMapping("/followers")
    public ApiResult<ArticleListResult> findFollowingArticles(@AuthenticationPrincipal JwtAuthentication authentication,
                                                              PageParameter pageParameter) {
        return success(
                new ArticleListResult(articleService.findFollowingArticles(authentication.id(), pageParameter))
        );
    }

    @GetMapping("/bookmark")
    public ApiResult<ArticleListResult> findBookmarkArticles(@AuthenticationPrincipal JwtAuthentication authentication,
                                                             PageParameter pageParameter) {
        return success(
                new ArticleListResult(articleService.findBookmarkArticles(authentication.id(), pageParameter))
        );
    }

    //TODO: 글 작성자만 변경 가능하게 하기
    @PutMapping("/{id}")
    public ApiResult<Boolean> update(@PathVariable Long id,
                                     @RequestPart UpdateRequest request,
                                     @RequestPart List<MultipartFile> images) {
        return success(articleService.update(id, article(request), images));
    }

    //TODO: 글 작성자만 삭제 가능하게 하기
    @DeleteMapping("/{id}")
    public ApiResult<Boolean> delete(@PathVariable Long id) {
        return success(articleService.delete(id));
    }

    private Article article(SaveRequest request, Long userId) {
        return Article.builder()
                .userId(userId)
                .content(request.content())
                .createdDate(now())
                .build();
    }

    private Article article(UpdateRequest request) {
        return Article.builder()
                .content(request.content())
                .build();
    }
}
