package github.maxsplawski.realworld.application.user;

import github.maxsplawski.realworld.application.auth.dto.AuthenticatedUserResponse;
import github.maxsplawski.realworld.application.user.dto.Profile;
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
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/api/user")
    public ResponseEntity<Map<String, AuthenticatedUserResponse>> updateUser(
            WebRequest request,
            Principal principal,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        User updatedUser = this.userService.updateUser(principal.getName(), updateUserRequest);

        Map<String, AuthenticatedUserResponse> responseBody = new HashMap<>();
        responseBody.put("user", new AuthenticatedUserResponse(
                updatedUser.getEmail(),
                request.getHeader("Authorization"),
                updatedUser.getUsername(),
                updatedUser.getBio(),
                updatedUser.getImage()
        ));

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/api/profiles/{username}")
    public ResponseEntity<Map<String, Profile>> getUserProfile(
            Principal principal,
            @PathVariable String username
    ) {
        Profile profile = this.userService.getUserProfile(username, Optional.ofNullable(principal));

        Map<String, Profile> responseBody = new HashMap<>();
        responseBody.put("profile", profile);

        return ResponseEntity.ok().body(responseBody);
    }


}
