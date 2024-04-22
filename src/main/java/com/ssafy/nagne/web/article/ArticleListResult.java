package com.ssafy.nagne.web.article;

import static org.springframework.beans.BeanUtils.copyProperties;

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

        private Long id;
        private Long userId;
        private String title;
        private String content;
        private Integer hit;
        private Integer good;
        private LocalDateTime createDate;

        public ArticleInfo(Article article) {
            copyProperties(article, this);
        }
    }
}
