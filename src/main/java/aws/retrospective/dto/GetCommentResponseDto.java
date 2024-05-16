package aws.retrospective.dto;

import aws.retrospective.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetCommentResponseDto {

    @Schema(description = "댓글 작성자 이름 이름", example = "김감자")
    private String username;
    @Schema(description = "댓글 작성 시간", example = "2024-05-10T00:00:00")
    private LocalDateTime createdDate;

    public static GetCommentResponseDto of(Comment comment) {
        return new GetCommentResponseDto(comment.getUser().getUsername(), comment.getCreatedDate());
    }
}
