package github.maxsplawski.realworld.application.article.service;

import github.maxsplawski.realworld.application.article.dto.ArticleList;
import github.maxsplawski.realworld.application.article.dto.CreateArticle;
import github.maxsplawski.realworld.application.article.dto.UpdateArticle;
import github.maxsplawski.realworld.domain.article.Article;
import github.maxsplawski.realworld.domain.article.ArticleRepository;
import github.maxsplawski.realworld.util.string.Slugger;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticleList getArticles(Pageable pageable) {
        Page<Article> page = this.articleRepository
                .findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Direction.DESC, "createdAt")
                        )
                );
        ArticleList articleList = new ArticleList(page.toList(), page.getNumberOfElements());

        return articleList;
    }

    public Article getArticle(String slug) {
        return this.articleRepository.findBySlugOrThrow(slug);
    }

    public Article createArticle(CreateArticle dto) {
        Article article = new Article();

        String title = dto.getTitle();
        String slug = Slugger.slugifyFrom(title);

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
        Article article = this.articleRepository.findBySlugOrThrow(slug);

        this.articleRepository.delete(article);
    }
}
