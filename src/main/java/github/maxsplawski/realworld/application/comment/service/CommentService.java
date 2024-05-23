package github.maxsplawski.realworld.application.comment.service;

import github.maxsplawski.realworld.application.comment.dto.CreateComment;
import github.maxsplawski.realworld.domain.article.Article;
import github.maxsplawski.realworld.domain.article.ArticleRepository;
import github.maxsplawski.realworld.domain.comment.Comment;
import github.maxsplawski.realworld.domain.comment.CommentRepository;
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
        Article article = articleRepository.findBySlug(articleSlug)
                .orElseThrow(() -> new EntityNotFoundException(articleSlug));

        return article.getComments();
    }

    public Comment createCommentForArticle(String articleSlug, CreateComment dto) {
        Article article = articleRepository.findBySlug(articleSlug)
                .orElseThrow(() -> new EntityNotFoundException(articleSlug));

        Comment comment = new Comment();
        comment.setBody(dto.getBody());

        article.addComment(comment);

        return commentRepository.save(comment);
    }
}
