package github.maxsplawski.realworld.application.article.dto;

import github.maxsplawski.realworld.domain.article.Article;

import java.util.List;

public class ArticleListResponse {
    private final List<Article> articles;

    private final int articlesCount;

    public ArticleListResponse(Builder builder) {
        this.articles = builder.articles;
        this.articlesCount = builder.articlesCount;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public int getArticlesCount() {
        return articlesCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<Article> articles;

        private int articlesCount;

        public Builder() {
        }

        public Builder articles(List<Article> articles) {
            this.articles = articles;
            return this;
        }

        public Builder articlesCount(int count) {
            this.articlesCount = count;
            return this;
        }

        public ArticleListResponse build() {
            return new ArticleListResponse(this);
        }
    }
}
