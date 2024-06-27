package github.maxsplawski.realworld.application.article.controller;

import github.maxsplawski.realworld.application.article.dto.ArticleListData;
import github.maxsplawski.realworld.application.article.dto.CreateArticleRequest;
import github.maxsplawski.realworld.application.article.dto.UpdateArticleRequest;
import github.maxsplawski.realworld.application.article.service.ArticleService;
import github.maxsplawski.realworld.application.article.service.CommentService;
import github.maxsplawski.realworld.configuration.security.JpaUserDetailsService;
import github.maxsplawski.realworld.configuration.security.SecurityConfiguration;
import github.maxsplawski.realworld.domain.article.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
@ContextConfiguration(classes = {ArticleController.class, SecurityConfiguration.class})
@WithMockUser
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private CommentService commentService;

    @Test
    public void returnsListOfArticles() throws Exception {
        List<Article> articles = Arrays.asList(
                new Article("Article 1", "article-1", "What's this about", "That's what's up"),
                new Article("Article 2", "article-2", "What's this about", "That's what's up"));
        ArticleListData articlesList = ArticleListData.builder()
                .articles(articles)
                .articlesCount(2)
                .build();

        when(this.articleService.getArticles(any(Pageable.class))).thenReturn(articlesList);

        mockMvc
                .perform(get("/api/articles")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.article").isArray())
                .andExpect(jsonPath("$.article", hasSize(2)))
                .andExpect(jsonPath("$.article[0].title").value("Article 1"))
                .andExpect(jsonPath("$.article[1].title").value("Article 2"));
    }

    @Test
    public void returnsArticleBySlug() throws Exception {
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
    public void createsArticle() throws Exception {
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
    public void updatesAnArticle() throws Exception {
        Article updatedArticle = new Article("Updated Article", "updated-article", "What's up", "That's what's up");

        when(this.articleService.updateArticle(any(String.class), any(UpdateArticleRequest.class))).thenReturn(updatedArticle);

        mockMvc
                .perform(patch("/api/articles/article")
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