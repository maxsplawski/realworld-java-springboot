package github.maxsplawski.realworld.application.article.controller;

import github.maxsplawski.realworld.application.article.dto.CreateCommentRequest;
import github.maxsplawski.realworld.application.article.service.CommentService;
import github.maxsplawski.realworld.application.user.service.JpaUserDetailsService;
import github.maxsplawski.realworld.configuration.security.SecurityConfiguration;
import github.maxsplawski.realworld.domain.article.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@ContextConfiguration(classes = {CommentController.class, SecurityConfiguration.class})
@WithMockUser
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    private CommentService commentService;

    @Test
    public void givenArticleHasNoComments_whenRequest_thenReturnsEmptyListOfComments() throws Exception {
        when(this.commentService.getArticleComments(any(String.class))).thenReturn(new ArrayList<>());

        mockMvc
                .perform(get("/api/articles/article/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    public void whenRequest_thenReturnsListOfComments() throws Exception {
        List<Comment> comments = List.of(new Comment("That's nice"), new Comment("Interesting"));

        when(this.commentService.getArticleComments(any(String.class))).thenReturn(comments);

        mockMvc
                .perform(get("/api/articles/article/comments")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comments").isArray())
                .andExpect(jsonPath("$.comments", hasSize(2)))
                .andExpect(jsonPath("$.comments[0].body").value("That's nice"))
                .andExpect(jsonPath("$.comments[1].body").value("Interesting"));
    }

    @Test
    public void createsACommentForAnArticle() throws Exception {
        Comment createdComment = new Comment("Nice!");

        when(this.commentService.createCommentForArticle(any(String.class), any(CreateCommentRequest.class))).thenReturn(createdComment);

        mockMvc
                .perform(post("/api/articles/article/comments")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        //language=json
                        .content("""
                                            {
                                                "body": "Nice!"
                                            }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.comment").isNotEmpty())
                .andExpect(jsonPath("$.comment.body").value("Nice!"));
    }

    @Test
    public void deletesACommentFromAnArticle() throws Exception {
        mockMvc
                .perform(delete("/api/articles/article/comments/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(this.commentService, times(1)).deleteArticleComment(any(String.class), any(Long.class));
    }
}