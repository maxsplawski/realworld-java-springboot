package github.maxsplawski.realworld.application.article.dto;

import github.maxsplawski.realworld.application.user.dto.ProfileData;

import java.time.Instant;

public class ArticleData {
    private final String title;

    private final String slug;

    private final String description;

    private final String body;

    private final Instant createdAt;

    private final Instant updatedAt;

    private final boolean favourited;

    private final int favouritesCount;

    private final ProfileData author;

    public ArticleData(Builder builder) {
        this.title = builder.title;
        this.slug = builder.slug;
        this.description = builder.description;
        this.body = builder.body;
        this.createdAt = builder.createdAt;
        this.updatedAt = builder.updatedAt;
        this.favourited = builder.favourited;
        this.favouritesCount = builder.favouritesCount;
        this.author = builder.author;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public boolean isFavourited() {
        return this.favourited;
    }

    public int getFavouritesCount() {
        return this.favouritesCount;
    }

    public ProfileData getAuthor() {
        return author;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "ArticleData{" +
                "title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", description='" + description + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", favourited=" + favourited +
                ", favouritesCount=" + favouritesCount +
                ", author=" + author +
                '}';
    }

    public static class Builder {
        private String title;

        private String slug;

        private String description;

        private String body;

        private Instant createdAt;

        private Instant updatedAt;

        private boolean favourited;

        private int favouritesCount;

        private ProfileData author;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder slug(String slug) {
            this.slug = slug;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder favourited(boolean favourited) {
            this.favourited = favourited;
            return this;
        }

        public Builder favouritesCount(int favouritesCount) {
            this.favouritesCount = favouritesCount;
            return this;
        }

        public Builder author(ProfileData author) {
            this.author = author;
            return this;
        }

        public ArticleData build() {
            return new ArticleData(this);
        }
    }
}
