package github.maxsplawski.realworld.application.article;

import github.maxsplawski.realworld.application.article.dto.CreateArticle;
import github.maxsplawski.realworld.application.article.dto.UpdateArticle;
import github.maxsplawski.realworld.application.article.service.ArticleService;
import github.maxsplawski.realworld.domain.article.Article;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public ResponseEntity<Map<String, List<Article>>> getArticles() {
        List<Article> articles = articleService.getArticles();

        Map<String, List<Article>> responseBody = new HashMap<>();
        responseBody.put("article", articles);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Map<String, Article>> getArticle(@PathVariable String slug) {
        Article article = this.articleService.getArticle(slug);

        Map<String, Article> responseBody = new HashMap<>();
        responseBody.put("article", article);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }

    @PostMapping("")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody CreateArticle dto) {
        Article createdArticle = this.articleService.createArticle(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdArticle);
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<Map<String, Article>> updateArticle(
            @PathVariable String slug,
            @Valid @RequestBody UpdateArticle dto
    ) {
        Article updatedArticle = this.articleService.updateArticle(slug, dto);

        Map<String, Article> responseBody = new HashMap<>();
        responseBody.put("article", updatedArticle);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseBody);
    }

    @DeleteMapping("/{slug}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String slug) {
        this.articleService.deleteArticle(slug);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
