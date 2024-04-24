package com.ssafy.nagne.web.comment;

import com.ssafy.nagne.domain.Comment;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SaveResult {

    private final CommentInfo commentInfo;

    public SaveResult(Comment comment) {
        this.commentInfo = new CommentInfo(comment);
    }

    @Data
    private static class CommentInfo {

        private final Long id;
        private final Long userId;
        private final LocalDateTime createdDate;

        public CommentInfo(Comment comment) {
            this.id = comment.getId();
            this.userId = comment.getUserId();
            this.createdDate = comment.getCreatedDate();
        }
    }
}
