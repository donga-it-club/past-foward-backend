package aws.retrospective.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentDto {

    private final Long id;
    private final String content;

    public String getComment() {
        return content;
    }
}
