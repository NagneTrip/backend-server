package com.ssafy.nagne.aop;

import static com.ssafy.nagne.domain.NotificationType.LIKE;
import static java.time.LocalDateTime.now;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.domain.LikeNotification;
import com.ssafy.nagne.domain.Notification;
import com.ssafy.nagne.service.ArticleService;
import com.ssafy.nagne.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LikeNotificationAop {

    private final NotificationService notificationService;
    private final ArticleService articleService;

    @AfterReturning(value = "execution(* com.ssafy.nagne.service.ArticleLikeService.like(..)) && args(userId, articleId)")
    public void sendFollowNotification(Long userId, Long articleId) {
        Article article = articleService.findById(userId, articleId);

        notificationService.save(createNotification(userId, article));
    }

    private Notification createNotification(Long userId, Article article) {
        return LikeNotification.builder()
                .type(LIKE)
                .fromUserId(userId)
                .toUserId(article.getUserId())
                .isNew(true)
                .articleId(article.getId())
                .createdDate(now())
                .build();
    }
}
