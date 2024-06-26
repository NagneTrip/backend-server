package com.ssafy.nagne.aop;

import static com.ssafy.nagne.domain.NotificationType.LIKE;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.domain.LikeNotification;
import com.ssafy.nagne.domain.Notification;
import com.ssafy.nagne.domain.User;
import com.ssafy.nagne.service.ArticleService;
import com.ssafy.nagne.service.NotificationService;
import com.ssafy.nagne.service.UserService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
@Transactional
@RequiredArgsConstructor
public class LikeNotificationAop {

    private final NotificationService notificationService;
    private final ArticleService articleService;
    private final UserService userService;

    @AfterReturning(value = "execution(* com.ssafy.nagne.service.ArticleLikeService.like(..)) && args(userId, articleId)")
    public void sendLikeNotification(Long userId, Long articleId) {
        notificationService.save(createNotification(userId, articleId));
    }

    private Notification createNotification(Long userId, Long articleId) {
        User user = userService.findById(userId);

        return LikeNotification.builder()
                .type(LIKE)
                .fromUserId(user.getId())
                .fromUserNickname(user.getNickname())
                .fromUserProfileImage(user.getProfileImage())
                .fromUserTier(user.getTier())
                .toUserId(getToUserId(userId, articleId))
                .isNew(true)
                .articleId(articleId)
                .createdDate(now())
                .build();
    }

    private Long getToUserId(Long userId, Long articleId) {
        Article article = articleService.findById(articleId, userId);

        return article.getUserId();
    }
}
