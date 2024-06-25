package github.maxsplawski.realworld.application.auth;

import github.maxsplawski.realworld.application.auth.service.JwtTokenService;
import github.maxsplawski.realworld.config.security.JpaUserDetailsService;
import github.maxsplawski.realworld.config.security.SecurityConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AuthController.class, SecurityConfiguration.class})
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Test
    public void returnsUnauthorizedResponseForAnUnauthenticatedUser() throws Exception {
        this.mockMvc
                .perform(get("/api/articles"))
                .andExpect(status().isUnauthorized());
    }
}