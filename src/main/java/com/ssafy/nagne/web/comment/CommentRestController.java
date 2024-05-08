package com.ssafy.nagne.web.comment;

import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.security.JwtAuthentication;
import com.ssafy.nagne.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/comments")
@RequiredArgsConstructor
public class CommentRestController {

    private final CommentService commentService;

    @PostMapping
    public SaveResult save(@AuthenticationPrincipal JwtAuthentication authentication,
                           @Valid @RequestBody SaveRequest request) {
        return new SaveResult(commentService.save(comment(request, authentication.id())));
    }

    @GetMapping
    public CommentListResult findCommentsByArticleId(@RequestParam Long articleId) {
        return new CommentListResult(commentService.findCommentsByArticleId(articleId));
    }

    @PatchMapping("/{id}")
    public Boolean update(@PathVariable Long id, @RequestBody UpdateRequest request) {
        return commentService.update(id, request.content());
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return commentService.delete(id);
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
