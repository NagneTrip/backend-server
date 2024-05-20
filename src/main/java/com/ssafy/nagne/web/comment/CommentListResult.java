package com.ssafy.nagne.web.comment;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.domain.Tier;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class CommentListResult {

    private final List<CommentInfo> comments;

    public CommentListResult(List<Comment> comments) {
        this.comments = comments.stream()
                .map(CommentInfo::new)
                .toList();
    }

    @Data
    private static class CommentInfo {

        private Long id;
        private Long articleId;
        private Long userId;
        private String userNickname;
        private String userProfileImage;
        private Tier userTier;
        private String content;
        private Integer likeCount;
        private Boolean isLiked;
        private LocalDateTime createdDate;

        public CommentInfo(Comment comment) {
            copyProperties(comment, this);
        }
    }
}
