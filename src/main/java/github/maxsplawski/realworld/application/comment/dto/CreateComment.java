package github.maxsplawski.realworld.application.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateComment {
    @NotBlank
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
