package com.ssafy.nagne.web.article;

import static org.springframework.beans.BeanUtils.copyProperties;

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

        private Long id;
        private Long userId;
        private String content;
        private Integer good;
        private LocalDateTime createdDate;

        public ArticleInfo(Article article) {
            copyProperties(article, this);
        }
    }
}
