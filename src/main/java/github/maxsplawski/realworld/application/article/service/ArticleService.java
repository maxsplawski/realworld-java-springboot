package github.maxsplawski.realworld.application.article.service;

import github.maxsplawski.realworld.application.article.dto.SaveArticle;
import github.maxsplawski.realworld.domain.article.Article;
import github.maxsplawski.realworld.domain.article.ArticleRepository;
import github.maxsplawski.realworld.util.string.Slugger;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article save(SaveArticle dto) {
        Article article = new Article();

        var title = dto.getTitle();
        var slug = Slugger.slugifyFrom(title);

        article.setTitle(title);
        article.setSlug(slug);
        article.setDescription(dto.getDescription());
        article.setBody(dto.getBody());

        return this.articleRepository.save(article);
    }
}
