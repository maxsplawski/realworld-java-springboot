package github.maxsplawski.realworld.application.auth.controller;

import github.maxsplawski.realworld.application.auth.dto.AuthenticatedUserData;
import github.maxsplawski.realworld.application.auth.dto.LoginRequest;
import github.maxsplawski.realworld.application.auth.dto.RegisterRequest;
import github.maxsplawski.realworld.application.auth.service.AuthService;
import github.maxsplawski.realworld.application.user.service.UserService;
import github.maxsplawski.realworld.domain.user.SecurityUserDetails;
import github.maxsplawski.realworld.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.Map;

@RestController
class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, AuthenticatedUserData>> login(@Valid @RequestBody LoginRequest loginRequest) {
        SecurityUserDetails userDetails = this.authService.login(loginRequest);
        User user = userDetails.getUser();

        AuthenticatedUserData authenticatedUserData = AuthenticatedUserData.builder()
                .email(user.getEmail())
                .token(userDetails.getToken())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.authService.wrapAuthenticatedUserData(authenticatedUserData));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, AuthenticatedUserData>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        this.userService.createUser(registerRequest);
        SecurityUserDetails userDetails = this.authService.login(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword()));
        User user = userDetails.getUser();

        AuthenticatedUserData authenticatedUserData = AuthenticatedUserData.builder()
                .email(user.getEmail())
                .token(userDetails.getToken())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.authService.wrapAuthenticatedUserData(authenticatedUserData));
    }

    @GetMapping("/api/user")
    public ResponseEntity<Map<String, AuthenticatedUserData>> getAuthenticatedUser(
            WebRequest request,
            Principal principal
    ) {
        User user = this.userService.getUser(principal.getName());

        AuthenticatedUserData authenticatedUserData = AuthenticatedUserData.builder()
                .email(user.getEmail())
                .token(request.getHeader("Authorization"))
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.authService.wrapAuthenticatedUserData(authenticatedUserData));
    }
}
