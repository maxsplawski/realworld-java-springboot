package github.maxsplawski.realworld.application.user.controller;

import github.maxsplawski.realworld.application.auth.dto.AuthenticatedUserData;
import github.maxsplawski.realworld.application.user.dto.ProfileData;
import github.maxsplawski.realworld.application.user.dto.UpdateUserRequest;
import github.maxsplawski.realworld.application.user.service.UserService;
import github.maxsplawski.realworld.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/api/user")
    public ResponseEntity<Map<String, AuthenticatedUserData>> updateUser(
            WebRequest request,
            Principal principal,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        User updatedUser = this.userService.updateUser(principal.getName(), updateUserRequest);

        Map<String, AuthenticatedUserData> responseBody = new HashMap<>();
        responseBody.put("user", AuthenticatedUserData.builder()
                .email(updatedUser.getEmail())
                .token(request.getHeader("Authorization"))
                .username(updatedUser.getUsername())
                .bio(updatedUser.getBio())
                .image(updatedUser.getImage())
                .build()
        );

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/api/profiles/{username}")
    public ResponseEntity<Map<String, ProfileData>> getUserProfile(
            Principal principal,
            @PathVariable String username
    ) {
        ProfileData profile = this.userService.getUserProfile(username, Optional.ofNullable(principal));

        Map<String, ProfileData> responseBody = new HashMap<>();
        responseBody.put("profile", profile);

        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/api/profiles/{username}/follow")
    private ResponseEntity<Map<String, ProfileData>> followUser(
            Principal principal,
            @PathVariable String username
    ) {
        ProfileData profile = this.userService.followUser(username, principal);

        Map<String, ProfileData> responseBody = new HashMap<>();
        responseBody.put("profile", profile);

        return ResponseEntity.ok().body(responseBody);
    }

    @DeleteMapping("/api/profiles/{username}/follow")
    private ResponseEntity<Map<String, ProfileData>> unfollowUser(
            Principal principal,
            @PathVariable String username
    ) {
        ProfileData profile = this.userService.unfollowUser(username, principal);

        Map<String, ProfileData> responseBody = new HashMap<>();
        responseBody.put("profile", profile);

        return ResponseEntity.ok().body(responseBody);
    }
}
