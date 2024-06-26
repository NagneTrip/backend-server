package com.ssafy.nagne.web.article;

import static org.springframework.beans.BeanUtils.copyProperties;

import com.ssafy.nagne.domain.Article;
import com.ssafy.nagne.domain.Tier;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ArticleListResultForNonLoginUser {

    private final List<ArticleInfo> articles;

    public ArticleListResultForNonLoginUser(List<Article> articles) {
        this.articles = articles.stream()
                .map(ArticleInfo::new)
                .toList();
    }

    @Data
    private static class ArticleInfo {

        private Long id;
        private Long userId;
        private String userNickname;
        private String userProfileImage;
        private Tier userTier;
        private String content;
        private Integer commentCount;
        private Integer likeCount;
        private List<String> imageUrls;
        private LocalDateTime createdDate;

        public ArticleInfo(Article article) {
            copyProperties(article, this);

            imageUrls = article.getImageUrls();
        }
    }
}
