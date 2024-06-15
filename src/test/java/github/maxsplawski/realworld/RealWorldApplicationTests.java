package github.maxsplawski.realworld;

import github.maxsplawski.realworld.application.article.ArticleController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RealWorldApplicationTests {
    @Autowired
    private ArticleController articleController;

    @Test
    void contextLoads() {
        assertThat(articleController).isNotNull();
    }

}
