package github.maxsplawski.realworld.application.article.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateArticle {
    @NotBlank(message = "The title is required")
    private String title;

    @NotBlank(message = "The description is required")
    private String description;

    @NotBlank(message = "The body is required")
    private String body;

    public CreateArticle(String title, String description, String body) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

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
