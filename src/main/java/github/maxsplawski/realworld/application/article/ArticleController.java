package github.maxsplawski.realworld.application.article;

import github.maxsplawski.realworld.application.article.dto.SaveArticle;
import github.maxsplawski.realworld.application.article.service.ArticleService;
import github.maxsplawski.realworld.domain.article.Article;
import github.maxsplawski.realworld.domain.article.ArticleRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @PostMapping("")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody SaveArticle dto) {
        var createdArticle = this.articleService.save(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdArticle);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Map<String, Article>> getArticle(@PathVariable String slug) {
        Optional<Article> optionalArticle = articleRepository.findBySlug(slug);

        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();

            Map<String, Article> responseBody = new HashMap<>();
            responseBody.put("article", article);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(responseBody);
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }
}
