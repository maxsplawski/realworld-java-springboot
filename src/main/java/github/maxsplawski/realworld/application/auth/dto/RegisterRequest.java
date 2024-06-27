package github.maxsplawski.realworld.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank
    private final String username;

    @NotBlank
    @Email
    private final String email;

    @NotBlank
    private final String password;

    private final String bio;

    private final String image;

    @JsonCreator
    public RegisterRequest(
            @JsonProperty("username") String username,
            @JsonProperty("email") String email,
            @JsonProperty("password") String password,
            @JsonProperty("bio") String bio,
            @JsonProperty("image") String image
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='RAW_PASSWORD'" +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
