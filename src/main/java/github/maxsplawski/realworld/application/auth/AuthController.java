package github.maxsplawski.realworld.application.auth;

import github.maxsplawski.realworld.application.auth.service.JwtTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final JwtTokenService jwtTokenService;

    public AuthController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(Authentication authentication) {
        String token = this.jwtTokenService.generateToken(authentication);
        
        return ResponseEntity.ok().body(token);
    }
}
