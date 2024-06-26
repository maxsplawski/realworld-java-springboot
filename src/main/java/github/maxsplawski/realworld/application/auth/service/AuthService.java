package github.maxsplawski.realworld.application.auth.service;

import github.maxsplawski.realworld.application.auth.dto.CreateUserRequest;
import github.maxsplawski.realworld.application.auth.dto.LoginRequest;
import github.maxsplawski.realworld.domain.user.SecurityUserDetails;
import github.maxsplawski.realworld.domain.user.User;
import github.maxsplawski.realworld.domain.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    public AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
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

    public User createUser(CreateUserRequest createUserRequest) {
        User user = new User();

        String encodedPassword = new BCryptPasswordEncoder().encode(createUserRequest.getPassword());

        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(encodedPassword);
        Optional.ofNullable(createUserRequest.getBio()).ifPresent(user::setBio);
        Optional.ofNullable(createUserRequest.getImage()).ifPresent(user::setImage);
        user.setRoles("ROLE_USER");

        return this.userRepository.save(user);
    }
}
