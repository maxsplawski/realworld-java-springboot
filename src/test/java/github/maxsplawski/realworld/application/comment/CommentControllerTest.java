package github.maxsplawski.realworld.application.comment;

import github.maxsplawski.realworld.SecurityConfiguration;
import github.maxsplawski.realworld.application.article.service.ArticleService;
import github.maxsplawski.realworld.application.comment.service.CommentService;
import github.maxsplawski.realworld.domain.comment.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {CommentController.class, SecurityConfiguration.class})
@WebMvcTest(CommentController.class)
@WithMockUser
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ArticleService articleService;

    @Test
    public void returnsListOfComments() throws Exception {
        List<Comment> comments = Arrays.asList(new Comment("That's nice"), new Comment("Interesting"));

        when(this.commentService.getArticleComments(any(String.class))).thenReturn(comments);

        mockMvc
                .perform(get("/api/articles/article/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("comments").isArray())
                .andExpect(jsonPath("comments", hasSize(2)))
                .andExpect(jsonPath("comments[0].body").value("That's nice"))
                .andExpect(jsonPath("comments[1].body").value("Interesting"));
    }
}