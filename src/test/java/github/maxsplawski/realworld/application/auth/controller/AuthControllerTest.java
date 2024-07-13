package github.maxsplawski.realworld.application.auth.controller;

import github.maxsplawski.realworld.application.auth.dto.AuthenticatedUserData;
import github.maxsplawski.realworld.application.auth.dto.LoginRequest;
import github.maxsplawski.realworld.application.auth.service.AuthService;
import github.maxsplawski.realworld.application.user.service.JpaUserDetailsService;
import github.maxsplawski.realworld.application.user.service.UserService;
import github.maxsplawski.realworld.configuration.security.SecurityConfiguration;
import github.maxsplawski.realworld.domain.user.SecurityUserDetails;
import github.maxsplawski.realworld.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {AuthController.class, SecurityConfiguration.class})
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    private UserService userService;

    @Test
    public void returnsUnauthorizedResponseForAnUnauthenticatedUser() throws Exception {
        this.mockMvc
                .perform(get("/api/user"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    public void returnsAnAuthenticatedUser() throws Exception {
        User user = new User(
                "test.user@mail.com",
                "TestUser",
                "123",
                "Hello, there!",
                "image.com/image",
                "ROLE_USER"
        );
        Map<String, AuthenticatedUserData> authenticatedUserData = Map.of(
                "user", AuthenticatedUserData.builder()
                        .email(user.getEmail())
                        .token("abcd")
                        .username(user.getUsername())
                        .bio(user.getBio())
                        .image(user.getImage())
                        .build()
        );

        when(this.userService.getUser(any(String.class))).thenReturn(user);
        when(this.authService.wrapAuthenticatedUserData(any(AuthenticatedUserData.class))).thenReturn(authenticatedUserData);

        this.mockMvc
                .perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").isNotEmpty())
                .andExpect(jsonPath("$.user.username").value("TestUser"));
    }

    @Test
    public void returnsAnAuthenticatedResponseFromLoginEndpoint() throws Exception {
        User user = new User(
                "john.doe@mail.com",
                "John Doe",
                new BCryptPasswordEncoder().encode("password"),
                "Hello, there!",
                "https://images.com/image",
                "ROLE_USER"
        );
        SecurityUserDetails userDetails = new SecurityUserDetails(user);
        Map<String, AuthenticatedUserData> authenticatedUserData = Map.of(
                "user", AuthenticatedUserData.builder()
                        .email(user.getEmail())
                        .token("abcd")
                        .username(user.getUsername())
                        .bio(user.getBio())
                        .image(user.getImage())
                        .build()
        );

        when(this.authService.login(any(LoginRequest.class))).thenReturn(userDetails);
        when(this.authService.wrapAuthenticatedUserData(any(AuthenticatedUserData.class))).thenReturn(authenticatedUserData);

        this.mockMvc
                .perform(post("/api/user/login")
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
                .andExpect(jsonPath("$.user").isNotEmpty())
                .andExpect(jsonPath("$.user.username").value("John Doe"));
    }
}