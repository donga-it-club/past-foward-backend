package aws.retrospective.dto;

import lombok.Getter;

@Getter
public class CommentDto {

    private Long id;
    private String content;

    public CommentDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}