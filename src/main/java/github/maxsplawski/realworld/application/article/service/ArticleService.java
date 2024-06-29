package github.maxsplawski.realworld.application.article.service;

import github.maxsplawski.realworld.application.article.dto.ArticleListData;
import github.maxsplawski.realworld.application.article.dto.CreateArticleRequest;
import github.maxsplawski.realworld.application.article.dto.UpdateArticleRequest;
import github.maxsplawski.realworld.domain.article.Article;
import github.maxsplawski.realworld.domain.article.ArticleRepository;
import github.maxsplawski.realworld.domain.article.Comment;
import github.maxsplawski.realworld.domain.article.CommentRepository;
import github.maxsplawski.realworld.util.string.Slugger;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public ArticleListData getArticles(Pageable pageable) {
        Page<Article> page = this.articleRepository
                .findAll(
                        PageRequest.of(
                                pageable.getPageNumber(),
                                pageable.getPageSize(),
                                Sort.by(Sort.Direction.DESC, "createdAt")
                        )
                );

        return ArticleListData.builder()
                .articles(page.getContent())
                .articlesCount(page.getTotalElements())
                .build();
    }

    public Article getArticle(String slug) {
        return this.articleRepository.findBySlugOrThrow(slug);
    }

    public Article createArticle(CreateArticleRequest createArticleRequest) {
        Article article = new Article();

        String title = createArticleRequest.getTitle();
        String slug = Slugger.slugifyFrom(title);

        article.setTitle(title);
        article.setSlug(slug);
        article.setDescription(createArticleRequest.getDescription());
        article.setBody(createArticleRequest.getBody());

        return this.articleRepository.save(article);
    }

    public Article updateArticle(String slug, UpdateArticleRequest updateArticleRequest) {
        return this.articleRepository.findBySlug(slug)
                .map(article -> {
                    Optional.ofNullable(updateArticleRequest.getTitle()).ifPresent(title -> {
                        article.setTitle(title);
                        article.setSlug(Slugger.slugifyFrom(title));
                    });

                    Optional.ofNullable(updateArticleRequest.getDescription()).ifPresent(article::setDescription);
                    Optional.ofNullable(updateArticleRequest.getBody()).ifPresent(article::setBody);

                    return this.articleRepository.save(article);
                })
                .orElseThrow(() -> new EntityNotFoundException(slug));
    }

    public void deleteArticle(String slug) {
        Article article = this.articleRepository.findBySlugOrThrow(slug);
        List<Comment> comments = article.getComments();

        this.commentRepository.deleteAll(comments);
        this.articleRepository.delete(article);
    }
}
