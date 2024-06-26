package github.maxsplawski.realworld.application.article.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class CreateArticleRequest {
    @NotBlank(message = "The title is required")
    private final String title;

    @NotBlank(message = "The description is required")
    private final String description;

    @NotBlank(message = "The body is required")
    private final String body;

    @JsonCreator
    public CreateArticleRequest(
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("body") String body
    ) {
        this.title = title;
        this.description = description;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getBody() {
        return body;
    }
}
