package github.maxsplawski.realworld.domain.article;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);
}
