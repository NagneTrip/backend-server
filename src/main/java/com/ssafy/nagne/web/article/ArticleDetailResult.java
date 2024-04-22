package com.ssafy.nagne.web.article;

import com.ssafy.nagne.domain.Article;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ArticleDetailResult {

    private final ArticleInfo articleInfo;

    public ArticleDetailResult(Article article) {
        this.articleInfo = new ArticleInfo(article);
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
