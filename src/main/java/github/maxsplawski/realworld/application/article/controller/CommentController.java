package github.maxsplawski.realworld.application.article.controller;

import github.maxsplawski.realworld.application.article.dto.CreateCommentRequest;
import github.maxsplawski.realworld.application.article.service.CommentService;
import github.maxsplawski.realworld.domain.article.Comment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{slug}/comments")
    public ResponseEntity<Map<String, List<Comment>>> getArticleComments(@PathVariable String slug) {
        List<Comment> comments = this.commentService.getArticleComments(slug);

        Map<String, List<Comment>> responseBody = new HashMap<>();
        responseBody.put("comments", comments);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }

    @PostMapping("/{slug}/comments")
    public ResponseEntity<Map<String, Comment>> createCommentForArticle(
            @PathVariable String slug,
            @Valid @RequestBody CreateCommentRequest createCommentRequest
    ) {
        Comment comment = this.commentService.createCommentForArticle(slug, createCommentRequest);

        Map<String, Comment> responseBody = new HashMap<>();
        responseBody.put("comment", comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBody);
    }

    @DeleteMapping("/{slug}/comments/{id}")
    public ResponseEntity<Void> deleteArticleComment(
            @PathVariable String slug,
            @PathVariable Long id
    ) {
        this.commentService.deleteArticleComment(slug, id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
