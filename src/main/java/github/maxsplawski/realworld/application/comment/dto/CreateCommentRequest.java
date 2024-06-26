package github.maxsplawski.realworld.application.comment.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class CreateCommentRequest {
    @NotBlank
    private final String body;

    public String getBody() {
        return body;
    }

    @JsonCreator
    public CreateCommentRequest(@JsonProperty("body") String body) {
        this.body = body;
    }
}
