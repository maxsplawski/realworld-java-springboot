package github.maxsplawski.realworld.application.user.controller;

import github.maxsplawski.realworld.application.exception.GlobalExceptionHandler;
import github.maxsplawski.realworld.application.user.dto.ProfileData;
import github.maxsplawski.realworld.application.user.dto.UpdateUserRequest;
import github.maxsplawski.realworld.application.user.service.JpaUserDetailsService;
import github.maxsplawski.realworld.application.user.service.UserService;
import github.maxsplawski.realworld.configuration.security.SecurityConfiguration;
import github.maxsplawski.realworld.domain.user.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class, SecurityConfiguration.class, GlobalExceptionHandler.class})
@WithMockUser
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JpaUserDetailsService jpaUserDetailsService;

    @MockBean
    private UserService userService;

    @Test
    public void whenUserDoesNotExist_thenReturns404AndUserIsNotUpdated() throws Exception {
        doThrow(EntityNotFoundException.class).when(this.userService).updateUser(any(String.class), any(UpdateUserRequest.class));

        this.mockMvc
                .perform(put("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        // language=json
                        .content("""
                                    {
                                        "email": "test.user@mail.com",
                                        "username": "TestUser",
                                        "password": "123"
                                    }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void whenValidData_thenUpdatesUser() throws Exception {
        User updatedUser = new User(
                "test.user@mail.com",
                "TestUser",
                "123",
                "Hello, there!",
                "image.com/image",
                "ROLE_USER"
        );

        when(this.userService.updateUser(any(String.class), any(UpdateUserRequest.class))).thenReturn(updatedUser);

        this.mockMvc
                .perform(put("/api/user")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        // language=json
                        .content("""
                                    {
                                        "email": "test.user@mail.com",
                                        "username": "TestUser",
                                        "password": "123"
                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user").isNotEmpty())
                .andExpect(jsonPath("$.user.username").value("TestUser"));
    }

    @Test
    public void whenUserDoesNotExist_thenReturns404AndUserProfileIsNotFetched() throws Exception {
        doThrow(EntityNotFoundException.class).when(this.userService).getUserProfile(any(String.class), any(Principal.class));

        this.mockMvc.perform(get("/api/profiles/{username}", "TestUser").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void whenUserExists_thenReturnsUserProfile() throws Exception {
        ProfileData profile = ProfileData.builder()
                .username("TestUser")
                .bio("Hello")
                .image("https://image.com")
                .following(false)
                .build();

        when(this.userService.getUserProfile(any(String.class), (any(Principal.class)))).thenReturn(profile);

        this.mockMvc.perform(get("/api/profiles/TestUser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile").isNotEmpty())
                .andExpect(jsonPath("$.profile.username").value("TestUser"));
    }

    @Test
    public void whenValidData_thenFollowsUser() throws Exception {
        ProfileData profile = ProfileData.builder()
                .username("TestUser")
                .bio("Hello")
                .image("https://image.com")
                .following(true)
                .build();

        when(this.userService.followUser(any(String.class), (any(Principal.class)))).thenReturn(profile);

        this.mockMvc
                .perform(post("/api/profiles/TestUser/follow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile").isNotEmpty())
                .andExpect(jsonPath("$.profile.username").value("TestUser"))
                .andExpect(jsonPath("$.profile.following").value(true));
    }

    @Test
    public void unfollowsAUser() throws Exception {
        ProfileData profile = ProfileData.builder()
                .username("TestUser")
                .bio("Hello")
                .image("https://image.com")
                .following(false)
                .build();

        when(this.userService.unfollowUser(any(String.class), (any(Principal.class)))).thenReturn(profile);

        this.mockMvc
                .perform(delete("/api/profiles/TestUser/unfollow"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.profile").isNotEmpty())
                .andExpect(jsonPath("$.profile.username").value("TestUser"))
                .andExpect(jsonPath("$.profile.following").value(false));
    }
}