package github.maxsplawski.realworld.application.comment;

import github.maxsplawski.realworld.application.comment.dto.CreateComment;
import github.maxsplawski.realworld.application.comment.service.CommentService;
import github.maxsplawski.realworld.domain.comment.Comment;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{slug}/comments")
    public ResponseEntity<Map<String, Comment>> createComment(
            @PathVariable String slug,
            @Valid @RequestBody CreateComment dto
    ) {
        Comment comment = commentService.createComment(slug, dto);

        Map<String, Comment> responseBody = new HashMap<>();
        responseBody.put("comment", comment);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBody);
    }
}
