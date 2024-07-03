package github.maxsplawski.realworld.application.article.controller;

import github.maxsplawski.realworld.application.article.dto.ArticleData;
import github.maxsplawski.realworld.application.article.dto.ArticleListData;
import github.maxsplawski.realworld.application.article.dto.CreateArticleRequest;
import github.maxsplawski.realworld.application.article.dto.UpdateArticleRequest;
import github.maxsplawski.realworld.application.article.service.ArticleService;
import github.maxsplawski.realworld.application.exception.GlobalExceptionHandler;
import github.maxsplawski.realworld.application.user.dto.ProfileData;
import github.maxsplawski.realworld.application.user.service.JpaUserDetailsService;
import github.maxsplawski.realworld.configuration.security.SecurityConfiguration;
import github.maxsplawski.realworld.domain.article.Article;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@ContextConfiguration(classes = {ArticleController.class, SecurityConfiguration.class, GlobalExceptionHandler.class})
@WithMockUser
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    private ArticleService articleService;

    @Test
    public void whenRequest_thenReturnsListOfArticles() throws Exception {
        List<ArticleData> articles = Arrays.asList(
                ArticleData.builder()
                        .title("Article 1")
                        .slug("article-1")
                        .description("What's this about")
                        .body("That's what's up")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .author(ProfileData.builder().build())
                        .build(),
                ArticleData.builder()
                        .title("Article 2")
                        .slug("article-2")
                        .description("What's this about")
                        .body("That's what's up")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .author(ProfileData.builder().build())
                        .build()
        );
        ArticleListData articlesList = ArticleListData.builder()
                .articles(articles)
                .articlesCount(2)
                .build();

        when(this.articleService.getArticles(any(Principal.class), any(Pageable.class))).thenReturn(articlesList);

        mockMvc
                .perform(get("/api/articles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles").isArray())
                .andExpect(jsonPath("$.articles", hasSize(2)))
                .andExpect(jsonPath("$.articles[0].title").value("Article 1"))
                .andExpect(jsonPath("$.articles[1].title").value("Article 2"))
                .andExpect(jsonPath("$.articlesCount").value(2));
    }

    @Test
    public void whenRequestWithPageable_thenReturnsPaginatedListOfArticles() throws Exception {
        List<ArticleData> articles = Collections.singletonList(
                ArticleData.builder()
                        .title("Article 2")
                        .slug("article-2")
                        .description("What's this about")
                        .body("That's what's up")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .author(ProfileData.builder().build())
                        .build()
        );
        ArticleListData articlesList = ArticleListData.builder()
                .articles(articles)
                .articlesCount(2)
                .build();

        when(this.articleService.getArticles(any(Principal.class), any(Pageable.class))).thenReturn(articlesList);

        mockMvc
                .perform(get("/api/articles?limit=1&offset=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.articles").isArray())
                .andExpect(jsonPath("$.articles", hasSize(1)))
                .andExpect(jsonPath("$.articles[0].title").value("Article 2"))
                .andExpect(jsonPath("$.articlesCount").value(2));
    }

    @Test
    public void whenValidSlug_thenReturnsArticle() throws Exception {
        Article article = new Article("Article", "article", "What's this about", "That's what's up");

        when(this.articleService.getArticle("article")).thenReturn(article);

        mockMvc
                .perform(get("/api/articles/article")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article").isNotEmpty())
                .andExpect(jsonPath("$.article.title").value("Article"))
                .andExpect(jsonPath("$.article.slug").value("article"));
    }

    @Test
    public void whenNoExistingArticle_thenReturns404() throws Exception {
        when(this.articleService.getArticle(any(String.class))).thenThrow(EntityNotFoundException.class);

        mockMvc
                .perform(get("/api/articles/not-existing-article")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenValidInput_thenCreatesArticle() throws Exception {
        Article createdArticle = new Article("Article", "article", "What's up", "That's what's up");

        when(this.articleService.createArticle(any(CreateArticleRequest.class))).thenReturn(createdArticle);

        mockMvc
                .perform(post("/api/articles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        //language=json
                        .content("""
                                    {
                                        "title": "Article",
                                        "description": "What's up",
                                        "body": "That's what's up"
                                    }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.article").isNotEmpty())
                .andExpect(jsonPath("$.article.title").value("Article"))
                .andExpect(jsonPath("$.article.description").value("What's up"));
    }

    @Test
    public void whenInvalidInput_thenReturns422() throws Exception {
        Article createdArticle = new Article("Article", "article", "What's up", "That's what's up");

        when(this.articleService.createArticle(any(CreateArticleRequest.class))).thenReturn(createdArticle);

        mockMvc
                .perform(post("/api/articles")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        //language=json
                        .content("""
                                    {
                                        "title": "Article"
                                    }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isNotEmpty())
                .andExpect(jsonPath("$.errors.description").isArray())
                .andExpect(jsonPath("$.errors.description[0]").value("The description is required"))
                .andExpect(jsonPath("$.errors.body").isArray())
                .andExpect(jsonPath("$.errors.body[0]").value("The body is required"));
    }

    @Test
    public void updatesAnArticle() throws Exception {
        Article updatedArticle = new Article("Updated Article", "updated-article", "What's up", "That's what's up");

        when(this.articleService.updateArticle(any(String.class), any(UpdateArticleRequest.class))).thenReturn(updatedArticle);

        mockMvc
                .perform(put("/api/articles/article")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        //language=json
                        .content("""
                                    {
                                        "title": "Updated Article",
                                        "description": "What's up",
                                        "body": "That's what's up"
                                     }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article").isNotEmpty())
                .andExpect(jsonPath("$.article.title").value("Updated Article"))
                .andExpect(jsonPath("$.article.description").value("What's up"));
    }

    @Test
    public void deletesAnArticle() throws Exception {
        mockMvc
                .perform(delete("/api/articles/article")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(this.articleService, times(1)).deleteArticle(any(String.class));
    }
}