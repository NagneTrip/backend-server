package com.ssafy.nagne.web.article;

import com.ssafy.nagne.domain.Article;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class ArticleListResult {

    private final List<ArticleInfo> articles;

    public ArticleListResult(List<Article> articles) {
        this.articles = articles.stream()
                .map(ArticleInfo::new)
                .toList();
    }

    @Data
    private static class ArticleInfo {

        private final Long id;
        private final Long userId;
        private final String title;
        private final String content;
        private final LocalDateTime createDate;

        public ArticleInfo(Article article) {
            this.id = article.getId();
            this.userId = article.getUserId();
            this.title = article.getTitle();
            this.content = article.getContent();
            this.createDate = article.getCreatedDate();
        }
    }
}
