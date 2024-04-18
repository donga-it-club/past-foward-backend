package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCommentDto {

    @Schema(description = "user id", example = "1")
    private Long userId;

    @Schema(description = "comment_section id", example = "2")
    private Long commentSectionId;

    @Schema(description = "댓글 내용", example = "새로운 댓글입니다.")
    private String commentContent;

    private LocalDateTime createDate;
}
