package com.ssafy.nagne.web.article;

import com.ssafy.nagne.domain.Article;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SaveResult {

    private final ArticleInfo articleInfo;

    public SaveResult(Article article) {
        this.articleInfo = new ArticleInfo(article);
    }

    @Data
    private static class ArticleInfo {

        private final Long id;
        private final Long userId;
        private final LocalDateTime createDate;

        public ArticleInfo(Article article) {
            this.id = article.getId();
            this.userId = article.getUserId();
            this.createDate = article.getCreatedDate();
        }
    }
}
