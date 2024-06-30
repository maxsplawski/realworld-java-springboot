package github.maxsplawski.realworld.application.article.dto;

import java.util.List;

public class ArticleListData {
    private final List<ArticleData> articles;

    private final long articlesCount;

    public ArticleListData(Builder builder) {
        this.articles = builder.articles;
        this.articlesCount = builder.articlesCount;
    }

    public List<ArticleData> getArticles() {
        return articles;
    }

    public long getArticlesCount() {
        return articlesCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "ArticleListData{" +
                "articles=" + articles +
                ", articlesCount=" + articlesCount +
                '}';
    }

    public static class Builder {
        private List<ArticleData> articles;

        private long articlesCount;

        public Builder() {
        }

        public Builder articles(List<ArticleData> articles) {
            this.articles = articles;
            return this;
        }

        public Builder articlesCount(long count) {
            this.articlesCount = count;
            return this;
        }

        public ArticleListData build() {
            return new ArticleListData(this);
        }
    }
}
