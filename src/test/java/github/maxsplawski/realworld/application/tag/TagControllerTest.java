package github.maxsplawski.realworld.application.tag;

import github.maxsplawski.realworld.SecurityConfiguration;
import github.maxsplawski.realworld.domain.tag.Tag;
import github.maxsplawski.realworld.domain.tag.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {TagController.class, SecurityConfiguration.class})
@WebMvcTest(TagController.class)
@WithMockUser
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagRepository tagRepository;

    @Test
    public void returnsAListOfTagNames() throws Exception {
        Iterable<Tag> tags = Arrays.asList(new Tag("java"), new Tag("rust"));

        when(this.tagRepository.findAll()).thenReturn(tags);

        mockMvc
                .perform(get("/api/tags")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("tags").isArray())
                .andExpect(jsonPath("tags", hasSize(2)))
                .andExpect(jsonPath("tags[0]").value("java"))
                .andExpect(jsonPath("tags[1]").value("rust"));
        ;
    }
}