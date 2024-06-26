package github.maxsplawski.realworld.application.auth.service;

import github.maxsplawski.realworld.application.auth.dto.LoginRequest;
import github.maxsplawski.realworld.domain.user.SecurityUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final TokenService jwtTokenService;

    public AuthService(AuthenticationManager authenticationManager, TokenService jwtTokenService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    public SecurityUserDetails login(LoginRequest loginRequest) {
        Authentication authenticationRequest = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUsername(), loginRequest.getPassword());
        Authentication authenticationResponse = this.authenticationManager.authenticate(authenticationRequest);

        String token = this.jwtTokenService.generateToken(authenticationResponse);
        SecurityUserDetails userDetails = (SecurityUserDetails) authenticationResponse.getPrincipal();
        userDetails.setToken(token);

        return userDetails;
    }
}
