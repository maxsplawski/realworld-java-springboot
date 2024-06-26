package github.maxsplawski.realworld.application.auth.dto;

public class AuthenticatedUserData {
    private final String email;

    private final String token;

    private final String username;

    private final String bio;

    private final String image;

    public AuthenticatedUserData(Builder builder) {
        this.email = builder.email;
        this.token = builder.token;
        this.username = builder.username;
        this.bio = builder.bio;
        this.image = builder.image;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public String getImage() {
        return image;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "AuthenticatedUserData{" +
                "email='" + email + '\'' +
                ", token='JWT_TOKEN'" +
                ", username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                '}';
    }

    public static class Builder {
        private String email;

        private String token;

        private String username;

        private String bio;

        private String image;

        public Builder() {
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        public AuthenticatedUserData build() {
            return new AuthenticatedUserData(this);
        }
    }
}
