package com.ssafy.nagne.web.comment;

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
        return new SaveResult(commentService.save(request, authentication.id()));
    }

    @GetMapping("/{id}")
    public CommentDetailResult findById(@PathVariable Long id,
                                        @AuthenticationPrincipal JwtAuthentication authentication) {
        return new CommentDetailResult(commentService.findById(id, authentication.id()));
    }

    @GetMapping
    public CommentListResult findCommentsByArticleId(@RequestParam Long articleId,
                                                     @AuthenticationPrincipal JwtAuthentication authentication) {
        return new CommentListResult(commentService.findCommentsByArticleId(articleId, authentication.id()));
    }

    @PatchMapping("/{id}")
    public Boolean update(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication authentication,
                          @Valid @RequestBody UpdateRequest request) {
        return commentService.update(id, authentication.id(), request.content());
    }

    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id, @AuthenticationPrincipal JwtAuthentication authentication) {
        return commentService.delete(id, authentication.id());
    }
}
