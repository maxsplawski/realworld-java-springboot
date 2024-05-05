package github.maxsplawski.realworld.application.article.service;

import github.maxsplawski.realworld.application.article.dto.SaveArticle;
import github.maxsplawski.realworld.domain.article.Article;
import github.maxsplawski.realworld.domain.article.ArticleRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(SaveArticle dto) {
        Article article = new Article();

        article.setTitle(dto.getTitle());
        article.setDescription(dto.getDescription());
        article.setBody(dto.getBody());

        return this.articleRepository.save(article);
    }
}
