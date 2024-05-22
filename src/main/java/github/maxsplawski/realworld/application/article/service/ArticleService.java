package github.maxsplawski.realworld.application.article.service;

import github.maxsplawski.realworld.application.article.dto.SaveArticle;
import github.maxsplawski.realworld.application.article.dto.UpdateArticle;
import github.maxsplawski.realworld.domain.article.Article;
import github.maxsplawski.realworld.domain.article.ArticleRepository;
import github.maxsplawski.realworld.util.string.Slugger;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getArticles() {
        List<Article> articles = new ArrayList<>();
        articleRepository.findAll().forEach(articles::add);

        return articles;
    }

    public Article getArticle(String slug) {
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(slug));
    }

    public Article createArticle(SaveArticle dto) {
        Article article = new Article();

        var title = dto.getTitle();
        var slug = Slugger.slugifyFrom(title);

        article.setTitle(title);
        article.setSlug(slug);
        article.setDescription(dto.getDescription());
        article.setBody(dto.getBody());

        return this.articleRepository.save(article);
    }

    public Article updateArticle(String slug, UpdateArticle dto) {
        return this.articleRepository.findBySlug(slug)
                .map(article -> {
                    Optional.ofNullable(dto.getTitle()).ifPresent(title -> {
                        article.setTitle(title);
                        article.setSlug(Slugger.slugifyFrom(title));
                    });

                    Optional.ofNullable(dto.getDescription()).ifPresent(article::setDescription);
                    Optional.ofNullable(dto.getBody()).ifPresent(article::setBody);

                    return this.articleRepository.save(article);
                })
                .orElseThrow(() -> new EntityNotFoundException(slug));
    }

    public void deleteArticle(String slug) {
        Optional<Article> optionalArticle = this.articleRepository.findBySlug(slug);

        optionalArticle.ifPresentOrElse(
                this.articleRepository::delete,
                () -> { throw new EntityNotFoundException(slug); }
        );
    }
}
