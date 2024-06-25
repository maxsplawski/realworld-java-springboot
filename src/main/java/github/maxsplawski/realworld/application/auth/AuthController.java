package github.maxsplawski.realworld.application.auth;

import github.maxsplawski.realworld.application.auth.dto.LoginRequest;
import github.maxsplawski.realworld.application.auth.dto.LoginResponse;
import github.maxsplawski.realworld.application.auth.dto.RegisterRequest;
import github.maxsplawski.realworld.application.auth.service.AuthService;
import github.maxsplawski.realworld.domain.user.SecurityUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        SecurityUserDetails userDetails = this.authService.login(loginRequest);

        Map<String, LoginResponse> responseBody = new HashMap<>();
        responseBody.put("user", new LoginResponse(
                userDetails.getUser().getEmail(),
                userDetails.getToken(),
                userDetails.getUsername(),
                userDetails.getUser().getBio(),
                userDetails.getUser().getImage()
        ));

        return ResponseEntity.ok().body(responseBody);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, LoginResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        SecurityUserDetails userDetails = this.authService.register(registerRequest);

        Map<String, LoginResponse> responseBody = new HashMap<>();
        responseBody.put("user", new LoginResponse(
                userDetails.getUser().getEmail(),
                userDetails.getToken(),
                userDetails.getUsername(),
                userDetails.getUser().getBio(),
                userDetails.getUser().getImage()
        ));

        return ResponseEntity.ok().body(responseBody);
    }
}
