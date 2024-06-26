package github.maxsplawski.realworld.application.article.dto;

import github.maxsplawski.realworld.domain.article.Article;

import java.util.List;

public class ArticleList {
    private List<Article> articles;

    private int articlesCount;

    public ArticleList(List<Article> articles, int articlesCount) {
        this.articles = articles;
        this.articlesCount = articlesCount;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getArticlesCount() {
        return articlesCount;
    }

    public void setArticlesCount(int articlesCount) {
        this.articlesCount = articlesCount;
    }
}
