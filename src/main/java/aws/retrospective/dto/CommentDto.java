package aws.retrospective.dto;

import lombok.Getter;

public class CommentDto {

    @Getter
    private final Long id;

    private final String content;

    public CommentDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getComment() {
        return content;
    }
}
