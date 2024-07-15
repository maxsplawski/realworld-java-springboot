package github.maxsplawski.realworld.application.article.controller;

import github.maxsplawski.realworld.application.user.service.JpaUserDetailsService;
import github.maxsplawski.realworld.configuration.security.SecurityConfiguration;
import github.maxsplawski.realworld.domain.article.Tag;
import github.maxsplawski.realworld.domain.article.TagRepository;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
@ContextConfiguration(classes = {TagController.class, SecurityConfiguration.class})
@WithMockUser
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    private TagRepository tagRepository;

    @Test
    public void whenNoTagsExist_thenReturnsEmptyListOfTagNames() throws Exception {
        when(this.tagRepository.findAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/tags").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(0)));
    }

    @Test
    public void whenRequest_thenReturnsListOfTagNames() throws Exception {
        List<Tag> tags = List.of(new Tag("java"), new Tag("rust"));

        when(this.tagRepository.findAll()).thenReturn(tags);

        mockMvc.perform(get("/api/tags").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tags").isArray())
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.tags[0]").value("java"))
                .andExpect(jsonPath("$.tags[1]").value("rust"));
        ;
    }
}