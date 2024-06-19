package github.maxsplawski.realworld.domain.article;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ArticleRepository extends CrudRepository<Article, Long> {
    Optional<Article> findBySlug(String slug);

    default Article findBySlugOrThrow(String slug) {
        return findBySlug(slug).orElseThrow(() -> new EntityNotFoundException(slug));
    }
}
