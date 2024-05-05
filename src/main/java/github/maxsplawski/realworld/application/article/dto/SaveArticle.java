package github.maxsplawski.realworld.application.article.dto;

import jakarta.validation.constraints.NotBlank;

public class SaveArticle {
    @NotBlank(message = "The title is required")
    private String title;

    @NotBlank(message = "The description is required")
    private String description;

    @NotBlank(message = "The body is required")
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
