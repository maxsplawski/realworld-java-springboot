package github.maxsplawski.realworld.application.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank(message = "The username is required")
    private final String username;

    @NotBlank(message = "The email is required")
    @Email(
            regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "The provided email is invalid"
    )
    private final String email;

    @NotBlank(message = "The password is required")
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
