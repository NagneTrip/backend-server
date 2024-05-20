package com.ssafy.nagne.web.article;

import com.ssafy.nagne.page.Pageable;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.ArticleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("api/articles")
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    @PostMapping
    public SaveResult save(@AuthenticationPrincipal JwtAuthentication authentication,
                           @Valid @RequestPart SaveRequest request,
                           @RequestPart List<MultipartFile> images) {
        return new SaveResult(articleService.save(request, authentication.id(), images));
    }

    @GetMapping("/{id}")
    public ArticleDetailResult findById(@PathVariable Long id,
                                        @AuthenticationPrincipal JwtAuthentication authentication) {
        return new ArticleDetailResult(articleService.findById(id, authentication.id()));
    }

    @GetMapping
    public ArticleListResultForNonLoginUser findAll(Pageable pageable) {
        return new ArticleListResultForNonLoginUser(articleService.findAll(pageable));
    }

    @GetMapping("/by")
    public ArticleListResult findArticlesByUserId(@RequestParam Long userId,
                                                  @AuthenticationPrincipal JwtAuthentication authentication,
                                                  Pageable pageable) {
        return new ArticleListResult(articleService.findArticlesByUserId(userId, authentication.id(), pageable));
    }

    @GetMapping("/tags")
    public ArticleListResult findArticlesByTags(@RequestParam List<String> tags,
                                                @AuthenticationPrincipal JwtAuthentication authentication,
                                                Pageable pageable) {
        return new ArticleListResult(articleService.findArticlesByTags(tags, authentication.id(), pageable));
    }

    @GetMapping("/followers")
    public ArticleListResult findFollowerArticles(@AuthenticationPrincipal JwtAuthentication authentication,
                                                  Pageable pageable) {
        return new ArticleListResult(articleService.findFollowerArticles(authentication.id(), pageable));
    }

    @GetMapping("/bookmark")
    public ArticleListResult findBookmarkArticles(@AuthenticationPrincipal JwtAuthentication authentication,
                                                  Pageable pageable) {
        return new ArticleListResult(articleService.findBookmarkArticles(authentication.id(), pageable));
    }

    //TODO: ArgumentResolver 리팩토링
    @GetMapping("/best")
    public BestArticleListResult findTop10Articles(@RequestParam String sort) {
        return new BestArticleListResult(articleService.findTop10Articles(sort));
    }

    @PatchMapping("/{id}")
    public Boolean update(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication authentication,
                          @Valid @RequestPart UpdateRequest request,
                          @RequestPart List<MultipartFile> images) {
        return articleService.update(id, authentication.id(), request, images);
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication authentication) {
        return articleService.delete(id, authentication.id());
    }
}
