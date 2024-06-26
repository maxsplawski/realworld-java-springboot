package github.maxsplawski.realworld.application.article.service;

import github.maxsplawski.realworld.application.article.dto.CreateCommentRequest;
import github.maxsplawski.realworld.domain.article.Article;
import github.maxsplawski.realworld.domain.article.ArticleRepository;
import github.maxsplawski.realworld.domain.article.Comment;
import github.maxsplawski.realworld.domain.article.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public List<Comment> getArticleComments(String articleSlug) {
        Article article = this.articleRepository.findBySlugOrThrow(articleSlug);

        return article.getComments();
    }

    public Comment createCommentForArticle(String articleSlug, CreateCommentRequest createCommentRequest) {
        Article article = this.articleRepository.findBySlugOrThrow(articleSlug);

        Comment comment = new Comment(createCommentRequest.getBody());

        article.addComment(comment);

        return this.commentRepository.save(comment);
    }

    public void deleteArticleComment(String articleSlug, Long commentId) {
        Article article = this.articleRepository.findBySlugOrThrow(articleSlug);

        Comment comment = article.getComments()
                .stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(commentId.toString()));

        this.commentRepository.delete(comment);
    }
}
