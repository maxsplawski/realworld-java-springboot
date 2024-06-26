package github.maxsplawski.realworld.application.user.service;

import github.maxsplawski.realworld.application.user.dto.CreateUserRequest;
import github.maxsplawski.realworld.application.user.dto.ProfileData;
import github.maxsplawski.realworld.application.user.dto.UpdateUserRequest;
import github.maxsplawski.realworld.domain.user.SecurityUserDetails;
import github.maxsplawski.realworld.domain.user.User;
import github.maxsplawski.realworld.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String username) {
        return this.userRepository.findByUsernameOrThrow(username);
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

    public User updateUser(String username, UpdateUserRequest updateUserRequest) {
        return this.userRepository.findByUsername(username)
                .map(user -> {
                    Optional.ofNullable(updateUserRequest.getUsername()).ifPresent(user::setUsername);
                    Optional.ofNullable(updateUserRequest.getEmail()).ifPresent(user::setEmail);
                    Optional.ofNullable(updateUserRequest.getPassword()).ifPresent(password -> {
                        String encodedPassword = new BCryptPasswordEncoder().encode(password);
                        user.setPassword(encodedPassword);
                    });
                    Optional.ofNullable(updateUserRequest.getBio()).ifPresent(user::setBio);
                    Optional.ofNullable(updateUserRequest.getImage()).ifPresent(user::setImage);

                    return this.userRepository.save(user);
                })
                .orElseThrow(() -> new EntityNotFoundException(username));
    }

    public ProfileData getUserProfile(String username, Optional<Principal> principal) {
        User queriedUser = this.userRepository.findByUsernameOrThrow(username);
        ProfileData profile = new ProfileData();
        profile.setFollowing(false);

        principal.ifPresent(p -> {
                    SecurityUserDetails authenticatedUserDetails = (SecurityUserDetails) p;
                    User authenticatedUser = authenticatedUserDetails.getUser();
                    profile.setFollowing(this.isFollowingUser(authenticatedUser, queriedUser));
                }
        );

        profile.setUsername(queriedUser.getUsername());
        profile.setBio(queriedUser.getBio());
        profile.setImage(queriedUser.getImage());

        return profile;
    }

    private boolean isFollowingUser(User follower, User followee) {
        return false;
    }
}
