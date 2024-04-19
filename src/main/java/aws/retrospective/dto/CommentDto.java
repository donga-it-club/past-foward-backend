package aws.retrospective.dto;

import lombok.Getter;

@Getter
public class CommentDto {

    private final Long id;
    private final String content;

    public CommentDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}