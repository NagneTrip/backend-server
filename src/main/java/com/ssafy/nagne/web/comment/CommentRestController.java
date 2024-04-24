package com.ssafy.nagne.web.comment;

import static com.ssafy.nagne.utils.ApiUtils.success;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.CommentService;
import com.ssafy.nagne.utils.ApiUtils.ApiResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    //TODO: CRUD 구현
    @PostMapping
    public ApiResult<SaveResult> save(@AuthenticationPrincipal JwtAuthentication authentication,
                                      @Valid @RequestBody SaveRequest request) {
        return success(new SaveResult(commentService.save(comment(request, authentication.id()))));
    }

    @GetMapping
    public ApiResult<CommentListResult> findCommentsByArticleId(@RequestParam Long articleId) {
        return success(new CommentListResult(commentService.findCommentsByArticleId(articleId)));
    }

    @PutMapping("/{id}")
    public ApiResult<Boolean> update(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return success(commentService.update(id, request.content()));
    }

    private Comment comment(SaveRequest request, Long userId) {
        return Comment.builder()
                .articleId(request.articleId())
                .userId(userId)
                .content(request.content())
                .createdDate(now())
                .build();
    }
}
