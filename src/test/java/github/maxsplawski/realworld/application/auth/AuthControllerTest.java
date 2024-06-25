package github.maxsplawski.realworld.application.auth;

import github.maxsplawski.realworld.application.auth.dto.LoginRequest;
import github.maxsplawski.realworld.application.auth.service.AuthService;
import github.maxsplawski.realworld.config.security.JpaUserDetailsService;
import github.maxsplawski.realworld.config.security.SecurityConfiguration;
import github.maxsplawski.realworld.domain.user.SecurityUserDetails;
import github.maxsplawski.realworld.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {AuthController.class, SecurityConfiguration.class})
@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    private AuthService authService;

    @Test
    public void returnsUnauthorizedResponseForAnUnauthenticatedUser() throws Exception {
        this.mockMvc
                .perform(get("/api/articles"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void returnsATokenFromLoginEndpoint() throws Exception {
        User user = new User(
                "john.doe@mail.com",
                "John Doe",
                new BCryptPasswordEncoder().encode("password"),
                "Hello, there!",
                "https://images.com/image",
                "ROLE_USER"
        );
        SecurityUserDetails userDetails = new SecurityUserDetails(user);

        when(this.authService.login(any(LoginRequest.class))).thenReturn(userDetails);

        this.mockMvc
                .perform(post("/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        // language=json
                        .content("""
                                    {
                                        "username": "John Doe",
                                        "password": "password"
                                    }
                                """))
                .andExpect(status().isOk())
                .andReturn();
    }
}