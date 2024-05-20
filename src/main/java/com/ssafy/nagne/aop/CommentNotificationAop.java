package com.ssafy.nagne.aop;

import static com.ssafy.nagne.domain.NotificationType.COMMENT;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Comment;
import com.ssafy.nagne.domain.CommentNotification;
import com.ssafy.nagne.domain.Notification;
import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.service.ArticleService;
import com.ssafy.nagne.service.NotificationService;
import com.ssafy.nagne.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Aspect
@Component
@Transactional
@RequiredArgsConstructor
public class CommentNotificationAop {

    private final NotificationService notificationService;
    private final ArticleService articleService;
    private final UserService userService;

    @AfterReturning(value = "execution(* com.ssafy.nagne.service.CommentService.save(..))", returning = "comment")
    public void sendCommentNotification(Comment comment) {
        notificationService.save(createNotification(comment));
    }

    private Notification createNotification(Comment comment) {
        User user = userService.findById(getFromUserId(comment));

        return CommentNotification.builder()
                .type(COMMENT)
                .fromUserId(user.getId())
                .fromUserNickname(user.getNickname())
                .fromUserProfileImage(user.getProfileImage())
                .fromUserTier(user.getTier())
                .toUserId(getToUserId(comment))
                .isNew(true)
                .articleId(getArticleId(comment))
                .commentId(getCommentId(comment))
                .createdDate(now())
                .build();
    }

    private Long getFromUserId(Comment comment) {
        return comment.getUserId();
    }

    private Long getToUserId(Comment comment) {
        return articleService.findById(comment.getArticleId(), comment.getUserId())
                .getUserId();
    }

    private Long getArticleId(Comment comment) {
        return comment.getArticleId();
    }

    private static Long getCommentId(Comment comment) {
        return comment.getId();
    }
}
