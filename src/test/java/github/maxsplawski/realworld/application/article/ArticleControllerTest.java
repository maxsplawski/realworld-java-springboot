package github.maxsplawski.realworld.application.article;

import github.maxsplawski.realworld.application.article.dto.CreateArticle;
import github.maxsplawski.realworld.application.article.service.ArticleService;
import github.maxsplawski.realworld.application.comment.service.CommentService;
import github.maxsplawski.realworld.domain.article.Article;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService articleService;

    @MockBean
    private CommentService commentService;

    @Test
    @WithMockUser
    public void returnsListOfArticles() throws Exception {
        // TODO: Check whether creating entities without explicitly providing id and timestamps is a good practice.
        List<Article> articles = Arrays.asList(
                new Article("Article 1", "article-1", "What's this about", "That's what's up"),
                new Article("Article 2", "article-2", "What's this about", "That's what's up"));

        when(this.articleService.getArticles()).thenReturn(articles);

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
    @WithMockUser
    public void returnsArticleBySlug() throws Exception {
        Article article = new Article("Article", "article", "What's this about", "That's what's up");

        when(this.articleService.getArticle("article")).thenReturn(article);

        mockMvc
                .perform(get("/api/articles/article")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("article").isNotEmpty())
                .andExpect(jsonPath("article.title").value("Article"))
                .andExpect(jsonPath("article.slug").value("article"));
    }

    @Test
    @WithMockUser
    public void createsArticle() throws Exception {
        CreateArticle dto = new CreateArticle("Article", "What's up", "That's what's up");
        Article createdArticle = new Article("Article", "article", "What's up", "That's what's up");

        when(this.articleService.createArticle(dto)).thenReturn(createdArticle);

        mockMvc
                .perform(post("/api/articles")
                        .with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .content(dto.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Article"));
    }
}