package github.maxsplawski.realworld.application.user.service;

import github.maxsplawski.realworld.application.auth.dto.RegisterRequest;
import github.maxsplawski.realworld.application.user.dto.ProfileData;
import github.maxsplawski.realworld.application.user.dto.UpdateUserRequest;
import github.maxsplawski.realworld.domain.user.User;
import github.maxsplawski.realworld.domain.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.lang.Nullable;
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

    public User createUser(RegisterRequest createUserRequest) {
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

    public ProfileData getUserProfile(String username, @Nullable Principal principal) {
        User queriedUser = this.userRepository.findByUsernameOrThrow(username);

        if (principal != null) {
            User authenticatedUser = this.userRepository.findByUsernameOrThrow(principal.getName());

            return ProfileData.builder()
                    .username(queriedUser.getUsername())
                    .bio(queriedUser.getBio())
                    .image(queriedUser.getImage())
                    .following(this.isFollowingUser(authenticatedUser, queriedUser))
                    .build();
        }

        return ProfileData.builder()
                .username(queriedUser.getUsername())
                .bio(queriedUser.getBio())
                .image(queriedUser.getImage())
                .following(false)
                .build();
    }

    public ProfileData followUser(String username, Principal principal) {
        User followee = this.userRepository.findByUsernameOrThrow(username);
        User follower = this.userRepository.findByUsernameOrThrow(principal.getName());

        follower.follow(followee);

        this.userRepository.save(follower);

        return ProfileData.builder()
                .username(followee.getUsername())
                .bio(followee.getBio())
                .image(followee.getImage())
                .following(this.isFollowingUser(follower, followee))
                .build();
    }

    public ProfileData unfollowUser(String username, Principal principal) {
        User followee = this.userRepository.findByUsernameOrThrow(username);
        User follower = this.userRepository.findByUsernameOrThrow(principal.getName());

        follower.unfollow(followee);

        this.userRepository.save(follower);

        return ProfileData.builder()
                .username(followee.getUsername())
                .bio(followee.getBio())
                .image(followee.getImage())
                .following(this.isFollowingUser(follower, followee))
                .build();
    }

    private boolean isFollowingUser(User follower, User followee) {
        return followee.getFollowers().contains(follower);
    }
}
