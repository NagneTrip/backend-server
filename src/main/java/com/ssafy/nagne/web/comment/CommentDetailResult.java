package com.ssafy.nagne.web.comment;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Comment;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class CommentDetailResult {

    private final CommentInfo commentInfo;

    public CommentDetailResult(Comment comment) {
        this.commentInfo = new CommentInfo(comment);
    }

    @Data
    private static class CommentInfo {

        private Long id;
        private Long articleId;
        private Long userId;
        private String content;
        private Integer likeCount;
        private Boolean isLiked;
        private LocalDateTime createdDate;

        public CommentInfo(Comment comment) {
            copyProperties(comment, this);
        }
    }
}
