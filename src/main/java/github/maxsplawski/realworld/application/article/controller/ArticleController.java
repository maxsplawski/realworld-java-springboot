package github.maxsplawski.realworld.application.article.controller;

import github.maxsplawski.realworld.application.article.dto.ArticleListData;
import github.maxsplawski.realworld.application.article.dto.CreateArticleRequest;
import github.maxsplawski.realworld.application.article.dto.UpdateArticleRequest;
import github.maxsplawski.realworld.application.article.service.ArticleService;
import github.maxsplawski.realworld.domain.article.Article;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("")
    public ResponseEntity<ArticleListData> getArticles(Pageable pageable) {
        ArticleListData articles = this.articleService.getArticles(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(articles);
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
    public ResponseEntity<Map<String, Article>> createArticle(@Valid @RequestBody CreateArticleRequest createArticleRequest) {
        Article createdArticle = this.articleService.createArticle(createArticleRequest);

        Map<String, Article> responseBody = new HashMap<>();
        responseBody.put("article", createdArticle);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBody);
    }

    @PatchMapping("/{slug}")
    public ResponseEntity<Map<String, Article>> updateArticle(
            @PathVariable String slug,
            @Valid @RequestBody UpdateArticleRequest updateArticleRequest
    ) {
        Article updatedArticle = this.articleService.updateArticle(slug, updateArticleRequest);

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
