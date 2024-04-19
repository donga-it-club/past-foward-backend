package aws.retrospective.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateCommentResponseDto {
    @Schema(description = "댓글 id", example = "1")
    private Long id;
    @Schema(description = "user id", example = "1")
    private Long userId;
    @Schema(description = "section id", example = "2")
    private Long sectionId;
    @Schema(description = "댓글 내용", example = "수고하셨습니다.")
    private String commentContent;

}
