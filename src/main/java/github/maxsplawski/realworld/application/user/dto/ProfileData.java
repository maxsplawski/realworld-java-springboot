package github.maxsplawski.realworld.application.user.dto;

public class ProfileData {
    private final String username;

    private final String bio;

    private final String image;

    private final boolean following;

    public ProfileData(Builder builder) {
        this.username = builder.username;
        this.bio = builder.bio;
        this.image = builder.image;
        this.following = builder.following;
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

    public boolean isFollowing() {
        return following;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "ProfileData{" +
                "username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", image='" + image + '\'' +
                ", following=" + following +
                '}';
    }

    public static class Builder {
        private String username;

        private String bio;

        private String image;

        private boolean following;

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

        public Builder following(boolean following) {
            this.following = following;
            return this;
        }

        public ProfileData build() {
            return new ProfileData(this);
        }
    }
}
