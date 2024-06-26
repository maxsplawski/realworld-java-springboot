package github.maxsplawski.realworld.application.article.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateArticleRequest {
    private final String title;

    private final String description;

    private final String body;

    @JsonCreator
    public UpdateArticleRequest(
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
