package github.maxsplawski.realworld.application.auth;

import github.maxsplawski.realworld.application.auth.dto.AuthenticatedUserResponse;
import github.maxsplawski.realworld.application.auth.dto.LoginRequest;
import github.maxsplawski.realworld.application.auth.service.AuthService;
import github.maxsplawski.realworld.application.user.dto.CreateUserRequest;
import github.maxsplawski.realworld.application.user.service.UserService;
import github.maxsplawski.realworld.domain.user.SecurityUserDetails;
import github.maxsplawski.realworld.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, AuthenticatedUserResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        SecurityUserDetails userDetails = this.authService.login(loginRequest);

        Map<String, AuthenticatedUserResponse> responseBody = new HashMap<>();
        responseBody.put("user", new AuthenticatedUserResponse(
                userDetails.getUser().getEmail(),
                userDetails.getToken(),
                userDetails.getUsername(),
                userDetails.getUser().getBio(),
                userDetails.getUser().getImage()
        ));

        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, AuthenticatedUserResponse>> register(@Valid @RequestBody CreateUserRequest registerRequest) {
        this.userService.createUser(registerRequest);
        SecurityUserDetails userDetails = this.authService.login(new LoginRequest(registerRequest.getUsername(), registerRequest.getPassword()));

        Map<String, AuthenticatedUserResponse> responseBody = new HashMap<>();
        responseBody.put("user", new AuthenticatedUserResponse(
                userDetails.getUser().getEmail(),
                userDetails.getToken(),
                userDetails.getUsername(),
                userDetails.getUser().getBio(),
                userDetails.getUser().getImage()
        ));

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping("/api/user")
    public ResponseEntity<Map<String, AuthenticatedUserResponse>> getAuthenticatedUser(
            WebRequest request,
            Principal principal
    ) {
        User user = this.userService.getUser(principal.getName());
        SecurityUserDetails userDetails = new SecurityUserDetails(user);

        Map<String, AuthenticatedUserResponse> responseBody = new HashMap<>();
        responseBody.put("user", new AuthenticatedUserResponse(
                userDetails.getUser().getEmail(),
                request.getHeader("Authorization"),
                userDetails.getUsername(),
                userDetails.getUser().getBio(),
                userDetails.getUser().getImage()
        ));

        return ResponseEntity.ok().body(responseBody);
    }
}
