package github.maxsplawski.realworld.application.article;

import github.maxsplawski.realworld.application.article.dto.SaveArticle;
import github.maxsplawski.realworld.application.article.service.ArticleService;
import github.maxsplawski.realworld.domain.article.Article;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("")
    public ResponseEntity<Article> createArticle(@Valid @RequestBody SaveArticle dto) {
        var createdArticle = this.articleService.save(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdArticle);
    }
}
